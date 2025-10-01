package kabinet.utils

fun <T> List<T>.replaceAt(index: Int, item: T) = mapIndexed { i, it -> if (i == index) item else it }

fun <T> List<T>.removeAt(index: Int) = mapIndexedNotNull { i, it -> if (i == index) null else it }