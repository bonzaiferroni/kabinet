package kabinet.gemini

import kabinet.clients.GeminiMessage
import kampfire.api.PostEndpoint
import kampfire.model.ImageGenRequest
import kampfire.model.ImageUrls
import kampfire.model.SpeechRequest

interface GeminiApi {
    val chat: PostEndpoint<List<GeminiMessage>, String>
    val image: PostEndpoint<ImageGenRequest, ImageUrls>
    val speechUrl: PostEndpoint<SpeechRequest, String>
    val speech: PostEndpoint<SpeechRequest, ByteArray>
}