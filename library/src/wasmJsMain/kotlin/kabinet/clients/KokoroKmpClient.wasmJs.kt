package kabinet.clients

import kampfire.model.KokoroVoice

actual class KokoroKmpClient actual constructor() {
    actual fun getMessage(text: String, voice: KokoroVoice): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun getCacheMessage(text: String, voice: KokoroVoice): ByteArray {
        TODO("Not yet implemented")
    }
}