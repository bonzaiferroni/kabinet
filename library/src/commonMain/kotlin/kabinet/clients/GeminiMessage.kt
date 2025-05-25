package kabinet.clients

import kotlinx.serialization.Serializable

@Serializable
data class GeminiMessage(
    val role: GeminiRole,
    val message: String,
)

enum class GeminiRole {
    User,
    Assistant
}