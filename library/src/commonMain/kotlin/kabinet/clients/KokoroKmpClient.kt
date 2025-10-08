package kabinet.clients

import kabinet.model.KokoroVoice

expect class KokoroKmpClient() {
    fun getMessage(text: String, voice: KokoroVoice = KokoroVoice.Bella): ByteArray
    fun getCacheMessage(text: String, voice: KokoroVoice = KokoroVoice.Sky): ByteArray
}