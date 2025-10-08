package kabinet.clients

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kabinet.console.globalConsole
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.Base64
import kotlin.time.Duration.Companion.seconds

private val console = globalConsole.getHandle(ReplicateClient::class)

class ReplicateClient(
    private val token: String,
    private val client: HttpClient = ktorClient
) {
    suspend fun request(
        url: String? = null,
        model: String? = null,
        input: ReplicateInput,
        method: HttpMethod = HttpMethod.Post,
        maxAttempts: Int = 3,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse? {
        repeat(maxAttempts) {
            try {
                val response = client.request {
                    this.method = method
                    url(url ?: "https://api.replicate.com/v1/predictions")
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer $token")
                    header("Prefer", "wait")
                    setBody(ReplicateRequestBody(
                        version = model,
                        input = input,
                    ))
                    block?.invoke(this)
                }
                if (response.status.value in 200..299) {
                    return response
                }
                if (response.status == HttpStatusCode.Conflict) {
                    delay(20.seconds)
                    return@repeat
                }
                console.logError("Replicate API ERROR > ${model}: ${response.status}\n$response")
                if (response.status.value in 400..499) return null
            } catch (e: Exception) {
                console.logError("Replicate Api: $e")
            }
            delay(3000)
        }
        console.logError("Replicate API ERROR > request fail: $${model}")
        return null
    }

    suspend fun requestBytes(
        url: String? = null,
        model: String? = null,
        input: ReplicateInput,
        method: HttpMethod = HttpMethod.Post,
        maxAttempts: Int = 3,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): ByteArray? {
        val response: ReplicateResponse = request(
            url = url,
            model = model,
            input = input,
            method = method,
            maxAttempts = maxAttempts,
            block = block
        )?.body() ?: return null
        val base64Part = response.output?.substringAfter(",") ?: return null
        return Base64.getDecoder().decode(base64Part)
    }

    suspend fun requestFileBytes(
        model: String,
        input: ReplicateInput,
        method: HttpMethod = HttpMethod.Post,
        maxAttempts: Int = 3,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): ByteArray? {
        val response: ReplicateResponse = request(
            model = model,
            input = input,
            method = method,
            maxAttempts = maxAttempts,
            block = block
        )?.body() ?: return null
        val fileUrl = response.output ?: return null
        return client.get(fileUrl).bodyAsBytes()
    }
}

private val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            // prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    defaultRequest {
        accept(ContentType.Application.Json)
        header(HttpHeaders.UserAgent, "ponder/1.0")
    }
    engine {
        requestTimeout = 300_000
    }
}