package kabinet.utils

private val ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
private val BASE = ALPHABET.length

fun Long.toBase62(): String {
    var num = this
    if (num == 0L) return ALPHABET[0].toString()
    val sb = StringBuilder()
    while (num > 0) {
        val rem = (num % BASE).toInt()
        sb.append(ALPHABET[rem])
        num /= BASE
    }
    return sb.reverse().toString()
}

fun String.fromBase62(): Long {
    var result = 0L
    for (c in this) {
        val idx = ALPHABET.indexOf(c)
        require(idx >= 0) { "Invalid char: $c" }
        result = result * BASE + idx
    }
    return result
}