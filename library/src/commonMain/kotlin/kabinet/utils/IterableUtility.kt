package kabinet.utils

inline fun <T> Iterable<T>.replace(
    newValue: T,
    predicate: (T) -> Boolean
): List<T> = map { if (predicate(it)) newValue else it }