package kabinet.utils

fun ShortArray.toWav(sampleRate: Int, channels: Int = 1): ByteArray {
    require(sampleRate > 0) { "sampleRate must be > 0" }
    require(channels >= 1) { "channels must be >= 1" }

    val bitsPerSample = 16
    val bytesPerSample = bitsPerSample / 8
    val byteRate = sampleRate * channels * bytesPerSample
    val blockAlign = channels * bytesPerSample

    val dataSize = this.size * bytesPerSample
    val riffChunkSize = 36 + dataSize
    val headerSize = 44
    val out = ByteArray(headerSize + dataSize)

    // RIFF header
    out.writeAscii(0, "RIFF")
    out.writeIntLE(4, riffChunkSize)
    out.writeAscii(8, "WAVE")

    // fmt chunk
    out.writeAscii(12, "fmt ")
    out.writeIntLE(16, 16)                 // PCM fmt chunk size
    out.writeShortLE(20, 1)                // PCM = 1
    out.writeShortLE(22, channels)
    out.writeIntLE(24, sampleRate)
    out.writeIntLE(28, byteRate)
    out.writeShortLE(32, blockAlign)
    out.writeShortLE(34, bitsPerSample)

    // data chunk
    out.writeAscii(36, "data")
    out.writeIntLE(40, dataSize)

    // samples (assumes this ShortArray is already interleaved if channels > 1)
    var p = headerSize
    for (s in this) {
        val v = s.toInt()
        out[p++] = (v and 0xFF).toByte()
        out[p++] = ((v ushr 8) and 0xFF).toByte()
    }
    return out
}

private fun ByteArray.writeAscii(offset: Int, text: String) {
    for (i in text.indices) this[offset + i] = text[i].code.toByte()
}

private fun ByteArray.writeIntLE(offset: Int, value: Int) {
    this[offset]     = ( value         and 0xFF).toByte()
    this[offset + 1] = ((value ushr 8) and 0xFF).toByte()
    this[offset + 2] = ((value ushr 16) and 0xFF).toByte()
    this[offset + 3] = ((value ushr 24) and 0xFF).toByte()
}

private fun ByteArray.writeShortLE(offset: Int, value: Int) {
    this[offset]     = ( value         and 0xFF).toByte()
    this[offset + 1] = ((value ushr 8) and 0xFF).toByte()
}