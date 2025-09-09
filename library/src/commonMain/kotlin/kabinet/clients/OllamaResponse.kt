package kabinet.clients

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed interface OllamaResponse {
    val totalDuration: Long?
    val loadDuration: Long?
    val promptEvalCount: Int?
}

@Serializable
data class OllamaGenerateResponse(
    val response: String? = null,
    val done: Boolean = false,
    val context: List<Int>? = null,
    @SerialName("total_duration")
    override val totalDuration: Long? = null,
    @SerialName("load_duration")
    override val loadDuration: Long? = null,
    @SerialName("prompt_eval_count")
    override val promptEvalCount: Int? = null,
    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    @SerialName("eval_duration")
    val evalDuration: Long? = null
): OllamaResponse

@Serializable
data class OllamaEmbedResponse(
    val model: String,
    val embeddings: List<FloatArray>,
    @SerialName("total_duration")
    override val totalDuration: Long? = null,
    @SerialName("load_duration")
    override val loadDuration: Long? = null,
    @SerialName("prompt_eval_count")
    override val promptEvalCount: Int? = null,
): OllamaResponse