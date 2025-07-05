package kabinet.utils

import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Frame.Binary.toObject() = this.readBytes()
    .let { Cbor.decodeFromByteArray<T>(it) }

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Frame.Binary.toObjectOrNull() = try {
    this.readBytes().let { Cbor.decodeFromByteArray<T>(it) }
} catch (e: Exception) {
    println("cbor error: ${e.message}")
    null
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> T.toFrame() = Frame.Binary(fin = true, data = Cbor.encodeToByteArray(this))

fun ByteArray.toFrame(fin: Boolean = true) = Frame.Binary(fin = fin, data = this)