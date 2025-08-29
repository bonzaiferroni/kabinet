package kabinet.clients

import io.ktor.utils.io.charsets.Charset
import kabinet.model.SpeechGenRequest
import kabinet.model.SpeechVoice

expect class KokoroKmpClient() {
    fun getMessage(text: String, voice: SpeechVoice = SpeechVoice.Sky): ByteArray
}