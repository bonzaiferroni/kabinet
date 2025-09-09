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
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kabinet.console.globalConsole
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

private val console = globalConsole.getHandle(OllamaClient::class)

class OllamaClient(
    private val client: HttpClient = ktorClient
) {
    private suspend fun request(
        request: OllamaRequest,
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
                        port = 11434
                        encodedPath = request.endpoint
                    }
                    contentType(ContentType.Application.Json)
                    setBody(request)
                    block?.invoke(this)
                }
                if (response.status == HttpStatusCode.OK) {
                    return response
                }
                console.logError("Ollama API ERROR > ${request.model}: ${response.status}")
            } catch (e: Exception) {
                console.logError("Ollama Api: $e")
            }
            delay(1000)
        }
        console.logError("Ollama API ERROR > request fail: $${request.model}")
        return null
    }

    suspend fun prompt(
        text: String,
        model: String = OllamaModel.Gemma3.apiLabel
    ): String? = request(
        OllamaGenerateRequest(
            model = model,
            prompt = text,
            stream = false,
        )
    )?.body<OllamaGenerateResponse>()?.response

    suspend fun streamPrompt(
        text: String,
        model: String = OllamaModel.Gemma3.apiLabel,
        onToken: suspend (String) -> Unit
    ): String? {
        val response = request(
            OllamaGenerateRequest(
                model = model,
                prompt = text,
                stream = true,
            )
        )
        val channel: ByteReadChannel = response?.bodyAsChannel() ?: return null

        var last: OllamaGenerateResponse? = null
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            if (line.isBlank()) continue

            val chunk = json.decodeFromString<OllamaGenerateResponse>(line)
            chunk.response?.let { onToken(it) }
            last = chunk

            if (chunk.done) break
        }
        return last?.response
    }

    suspend fun embed(
        text: String,
        model: String = OllamaModel.NomicEmbed.apiLabel
    ) = request(
        OllamaEmbedRequest(
            model = model,
            input = text
        )
    )?.body<OllamaEmbedResponse>()
}

private val json = Json { ignoreUnknownKeys = true }

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

enum class OllamaModel(val apiLabel: String) {
    Gemma3("gemma3"),
    GptOss("gpt-oss"),
    DeepSeekR1("deepseek-r1"),
    Qwen3("qwen3"),
    Llama31("llama3.1"),
    NomicEmbed("nomic-embed-text"),
    Mistral("mistral"),
    Phi3("phi3")
}