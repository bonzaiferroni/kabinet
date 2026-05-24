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
inline fun <reified T> Frame.Binary.toObjectOrNull() = readBytes().toObjectOrNull<T>()

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> T.toFrame() = Frame.Binary(fin = true, data = Cbor.encodeToByteArray(this))

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> T.toBytes() = Cbor.encodeToByteArray(this)

fun ByteArray.toFrame(fin: Boolean = true) = Frame.Binary(fin = fin, data = this)

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> ByteArray.toObjectOrNull() = try {
    Cbor.decodeFromByteArray<T>(this)
} catch (e: Exception) {
    println("cbor error: ${e.message}")
    null
}