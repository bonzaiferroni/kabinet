package kabinet.db

interface TableId {
    val value: String

    val isEmpty get() = value.isEmpty()
}