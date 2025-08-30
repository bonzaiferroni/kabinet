package kabinet.clients

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaRequest(
    val model: String,
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
)

@Serializable
data class OllamaResponse(
    val response: String? = null,
    val done: Boolean = false,
    val context: List<Int>? = null,
    @SerialName("total_duration")
    val totalDuration: Long? = null,
    @SerialName("load_duration")
    val loadDuration: Long? = null,
    @SerialName("prompt_eval_count")
    val promptEvalCount: Int? = null,
    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    @SerialName("eval_duration")
    val evalDuration: Long? = null
)