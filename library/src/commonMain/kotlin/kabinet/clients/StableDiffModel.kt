package kabinet.clients

import kotlinx.serialization.Serializable

@Serializable
data class StableDiffRequest(
    val prompt: String,
    val steps: Int,
)

@Serializable
data class StableDiffResponse(
    val images: List<String>,
    val info: String
)