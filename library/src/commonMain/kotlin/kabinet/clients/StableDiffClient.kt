package kabinet.clients

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kabinet.console.globalConsole
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

private val console = globalConsole.getHandle(StableDiffClient::class)

class StableDiffClient(
    private val client: HttpClient = ktorClient
) {
    private suspend fun request(
        request: StableDiffRequest,
        method: HttpMethod = HttpMethod.Post,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse? {
        repeat(1) {
            try {
                val response = client.request {
                    this.method = method
                    url {
                        protocol = URLProtocol.HTTP
                        host = "localhost"
                        port = 7860
                        encodedPath = "/sdapi/v1/txt2img"
                    }
                    contentType(ContentType.Application.Json)
                    setBody(request)
                    block?.invoke(this)
                }
                if (response.status == HttpStatusCode.OK) {
                    return response
                }
                console.logError("StableDiff API ERROR: ${response.status}")
            } catch (e: Exception) {
                console.logError("StableDiff Api: $e")
            }
            delay(1000)
        }
        console.logError("StableDiff API ERROR > request fail: ${request.prompt}")
        return null
    }

    suspend fun prompt(text: String, steps: Int = 20) = request(StableDiffRequest(
        prompt = text,
        steps = steps
    ))?.body<StableDiffResponse>()
}

private val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    defaultRequest {
        accept(ContentType.Application.Json)
        header(HttpHeaders.UserAgent, "ember/1.0")
    }
    engine {
        requestTimeout = 300_000 // Timeout in milliseconds (30 seconds here)
    }
}