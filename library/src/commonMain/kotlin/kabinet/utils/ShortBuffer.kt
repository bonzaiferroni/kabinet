package kabinet.utils

class ShortBuffer(initialCapacity: Int = 4096) {

    private var data = ShortArray(initialCapacity)
    private var index = 0

    fun append(samples: ShortArray) {
        ensureCapacity(index + samples.size)
        samples.copyInto(data, destinationOffset = index)
        index += samples.size
    }

    fun toArray(): ShortArray =
        data.copyOfRange(0, index)

    private fun ensureCapacity(minCapacity: Int) {
        if (minCapacity > data.size) {
            val newCapacity = maxOf(data.size * 2, minCapacity)
            data = data.copyOf(newCapacity)
        }
    }

    val size: Int get() = index
    val capacity: Int get() = data.size
    fun clear() { index = 0 }
}