package kabinet.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AudioStage(
    private val sampleSize: Int = 44_100,
) {
    private val layers = mutableListOf<AudioLayer>()
    private var position = 0
    private var isPlaying = false

    // work buffers, reused, no new allocations at play time
    private val layerSample = ShortArray(sampleSize)
    private val stage = ShortArray(sampleSize)      // will be one ping buffer
    private val liveSample = ShortArray(sampleSize) // will be the other ping buffer

    fun add(layer: AudioLayer) {
        layers.add(layer)
    }

    suspend fun play(
        listener: (ShortArray) -> Unit
    ) = coroutineScope {
        isPlaying = true
        position = 0

        while (isPlaying) {
            println("generating $position")
            stage.fill(0)
            var hasContent = false
            layers.forEach { layer ->
                if (layer.hasContent(position)) {
                    hasContent = true
                    layer.sampleAt(position, layerSample)
                    stage.forEachIndexed { i, value ->
                        stage[i] = addShorts(layerSample[i], value)
                    }
                }
            }
            if (hasContent) {
                position += sampleSize
            } else {
                isPlaying = false
            }

            if (position > 0) {
                stage.copyInto(liveSample)
                println("playing $position")
                listener(liveSample)
            }
        }

        println("finished")
    }
}

private fun addShorts(a: Short, b: Short) = (a + b).coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()

interface AudioLayer {
    fun hasContent(position: Int): Boolean
    fun sampleAt(position: Int, buffer: ShortArray)
}

class PcmLayer(
    private val pcm: ShortArray,
): AudioLayer {

    override fun hasContent(position: Int) = position < pcm.size

    override fun sampleAt(position: Int, buffer: ShortArray) {
        val len = buffer.size
        val available = (pcm.size - position).coerceAtLeast(0)
        val toCopy = len.coerceAtMost(available)
        if (toCopy > 0) {
            pcm.copyInto(buffer, 0, position, position + toCopy)
        }
        if (toCopy < len) {
            // zero fill remainder
            buffer.fill(0, toCopy, len)
        }
    }
}