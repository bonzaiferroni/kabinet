package kabinet.clients

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface OllamaRequest {
    val model: String
    val endpoint: String
}

@Serializable
data class OllamaEmbedRequest(
    override val model: String,
    val input: String
): OllamaRequest {
    override val endpoint get() = "api/embed"
}

@Serializable
data class OllamaGenerateRequest(
    override val model: String,
    val prompt: String? = null,
    val suffix: String? = null,
    val images: List<String>? = null,

    // Advanced parameters
    val format: String? = null,
    // val options: Map<String, Any?>? = null,
    val system: String? = null,
    val template: String? = null,
    val context: List<Int>? = null,
    val stream: Boolean? = null,
    val raw: Boolean? = null,
    @SerialName("keep_alive")
    val keepAlive: String? = null
): OllamaRequest {
    override val endpoint get() = "api/generate"
}