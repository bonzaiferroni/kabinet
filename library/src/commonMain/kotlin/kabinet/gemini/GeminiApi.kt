package kabinet.gemini

import kabinet.api.PostEndpoint
import kabinet.clients.GeminiMessage
import kabinet.model.ImageGenRequest
import kabinet.model.ImageUrls
import kabinet.model.SpeechRequest

interface GeminiApi {
    val chat: PostEndpoint<List<GeminiMessage>, String>
    val image: PostEndpoint<ImageGenRequest, ImageUrls>
    val speechUrl: PostEndpoint<SpeechRequest, String>
    val speech: PostEndpoint<SpeechRequest, ByteArray>
}