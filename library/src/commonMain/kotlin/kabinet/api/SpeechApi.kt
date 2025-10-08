package kabinet.api

import kabinet.model.SpeechRequest

interface SpeechApi {
    val wav: PostEndpoint<SpeechRequest, ByteArray>
    val url: PostEndpoint<SpeechRequest, String>
}