package kabinet.gemini

import kabinet.api.PostEndpoint
import kabinet.clients.GeminiMessage
import kabinet.model.ImageGenRequest
import kabinet.model.ImageUrls
import kabinet.model.SpeechGenRequest

interface GeminiApi {
    val chat: PostEndpoint<List<GeminiMessage>, String>
    val image: PostEndpoint<ImageGenRequest, ImageUrls>
    val speech: PostEndpoint<SpeechGenRequest, String>
}