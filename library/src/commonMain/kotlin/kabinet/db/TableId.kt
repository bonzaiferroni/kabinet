package kabinet.db

interface TableId<T> {
    val value: T
}

val TableId<String>.isEmpty get() = value.isEmpty()