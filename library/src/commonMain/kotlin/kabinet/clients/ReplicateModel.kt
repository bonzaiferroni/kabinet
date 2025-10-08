package kabinet.clients

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReplicateResponse(
    val id: String? = null,
    val model: String? = null,
    val version: String? = null,
    val input: ReplicateInputText? = null,
    val output: String? = null,
    val logs: String? = null,
    val error: String? = null,
    val status: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("completed_at") val completedAt: String? = null,
    val urls: ReplicateUrls? = null,
)

@Serializable
data class ReplicateInputText(
    val text: String
)

@Serializable
data class ReplicateUrls(
    val web: String,
    val cancel: String,
    val get: String
)

@Serializable
data class ReplicateRequestBody(
    val input: ReplicateInput,
    val version: String?,
)

@Serializable
data class ReplicateInput(
    val text: String,
    @SerialName("top_p") val topP: Double? = null,
    val voice: String? = null,
    val temperature: Double? = null,
    @SerialName("max_new_tokens") val maxNewTokens: Int? = null,
    @SerialName("repetition_penalty") val repetitionPenalty: Double? = null
)