package kabinet.utils

import kotlin.collections.forEachIndexed

class AudioStage {
    val pcm = ShortArray(44_100 * 10)

    fun add(pcm: ShortArray) {
        this.pcm.forEachIndexed { index, value ->
            if (index >= pcm.size) return
            this.pcm[index] = addShorts(pcm[index], value)
        }
    }

    val isEmpty = pcm.all { it == 0.toShort() }
}

fun addShorts(a: Short, b: Short) = (a + b).coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()