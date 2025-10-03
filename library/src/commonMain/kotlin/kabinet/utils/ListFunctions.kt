package kabinet.utils

fun <T> List<T>.replaceAt(index: Int, item: T) = mapIndexed { i, it -> if (i == index) item else it }

fun <T> List<T>.removeAt(index: Int) = mapIndexedNotNull { i, it -> if (i == index) null else it }

fun <T> List<T>.moveLeft(index: Int) = if (index == 0) this else mapIndexedNotNull { i, it ->
    when (i) {
        index - 1 -> this[index]
        index -> this[index - 1]
        else -> it
    }
}

fun <T> List<T>.moveRight(index: Int) = if (index == size - 1) this else mapIndexedNotNull { i, it ->
    when (i) {
        index -> this[index + 1]
        index + 1 -> this[index]
        else -> it
    }
}