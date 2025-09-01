package kabinet.clients

import io.ktor.utils.io.charsets.Charset
import kabinet.model.SpeechVoice
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread
import kotlin.use

actual class KokoroKmpClient {

    private val messages = mutableMapOf<String, ByteArray>()

    actual fun getMessage(text: String, voice: SpeechVoice) = runPyBytes("../py/speak.py", text, voice.apiName)

    actual fun getCacheMessage(text: String, voice: SpeechVoice) = messages[text]
        ?: getMessage(text, voice).also { messages[text] = it }

    fun runPyBytes(script: String, vararg args: String?, python: String = "python3"): ByteArray {
        val cmd = listOf(python, script) + args
        val proc = ProcessBuilder(cmd).start()

        val out = ByteArrayOutputStream()
        val err = ByteArrayOutputStream()

        val tOut = thread(start = true, name = "py-stdout") { proc.inputStream.use { it.copyTo(out) } }
        val tErr = thread(start = true, name = "py-stderr") { proc.errorStream.use { it.copyTo(err) } }

        val code = proc.waitFor()
        tOut.join()
        tErr.join()

        if (code != 0) {
            val errText = err.toByteArray().toString(Charset.defaultCharset())
            error("Python exited $code:\n$errText")
        }
        return out.toByteArray()
    }
}