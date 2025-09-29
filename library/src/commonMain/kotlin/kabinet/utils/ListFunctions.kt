package kabinet.utils

fun <T> List<T>.replaceAt(index: Int, item: T) = mapIndexed { i, it -> if (i == index) item else it }