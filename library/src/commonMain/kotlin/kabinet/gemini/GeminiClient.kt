package kabinet.gemini

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kabinet.console.LogLevel
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.time.Duration.Companion.seconds

class GeminiClient(
    val token: String,
    val backupToken: String? = null,
    val model: String = "gemini-2.5-flash",
    val client: HttpClient = ktorClient,
    val logMessage: (LogLevel, String) -> Unit
) {
    suspend inline fun <reified T> tryRequest(requestBlock: suspend () -> GeminiApiRequest<T>): HttpResponse? {
        var usedToken = token
        repeat(3) {
            try {
                val request = requestBlock()
                val model = request.model ?: model
                val url = generateUrl(model, request.method)
                val ktorRequest = HttpRequestBuilder().apply {
                    method = HttpMethod.Post
                    url(url)
                    header("x-goog-api-key", usedToken)
                    contentType(ContentType.Application.Json)
                    setBody(request.body)
                }
                val response = client.request(ktorRequest)
                if (response.status == HttpStatusCode.TooManyRequests) {
                    if (usedToken == backupToken) {
                        logMessage(LogLevel.Error, "GeminiClient: Too many requests received using backup token")
                        return null
                    }
                    logMessage(LogLevel.Info, "GeminiClient: Too many requests, trying backup token")
                    usedToken = backupToken ?: return null
                    return@repeat
                }
                if (response.status == HttpStatusCode.InternalServerError) {
                    logMessage(LogLevel.Error, "Internal server error, delaying")
                    delay(10.seconds)
                    return@repeat
                }
                if (response.status != HttpStatusCode.OK) {
                    logMessage(LogLevel.Error, "Error generating Json:\n${response.body<JsonObject>()}")
                }
                return response
            } catch (e: HttpRequestTimeoutException) {
                logMessage(LogLevel.Error, "Request timed out")
                return null
            } catch (e: NoTransformationFoundException) {
                logMessage(LogLevel.Error, "no transformation? 😕\n${e.message}")
                return null
            } catch (e: Exception) {
                logMessage(LogLevel.Error, "generateJson exception:\n${e.message}")
                return null
            }
        }
        return null
    }

    fun generateUrl(model: String, method: String) =
        "https://generativelanguage.googleapis.com/v1beta/models/$model:$method"
}

data class GeminiApiResponse<T>(
    val status: HttpStatusCode?,
    val data: T?
)

data class GeminiApiRequest<T>(
    val method: String,
    val body: T,
    val model: String? = null
)

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>,
)

@Serializable
data class GeminiPart(
    val text: String? = null,
    val inlineData: GeminiBlob? = null,
)

@Serializable
data class GeminiBlob(
    val mimeType: String? = null,
    val data: String, //  (bytes format) Raw bytes for media formats. A base64-encoded string.
)

private val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        headers {
            set(HttpHeaders.ContentType, "application/json")
        }
    }
    engine {
        requestTimeout = 120_000 // Timeout in milliseconds (30 seconds here)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 120_000 // Set request timeout
        connectTimeoutMillis = 120_000 // Set connection timeout
        socketTimeoutMillis = 120_000  // Set socket timeout
    }
}

