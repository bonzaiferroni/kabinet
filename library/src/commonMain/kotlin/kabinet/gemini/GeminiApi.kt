package kabinet.gemini

import kabinet.api.PostEndpoint
import kabinet.clients.GeminiMessage
import kabinet.model.ImageUrls
import kabinet.model.SpeechRequest

interface GeminiApi {
    val chat: PostEndpoint<List<GeminiMessage>, String>
    val image: PostEndpoint<String, ImageUrls>
    val speech: PostEndpoint<SpeechRequest, String>
}