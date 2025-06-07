package kabinet.utils

const val BASE = 62
val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

fun Long.toBase62(): String {
    var u = this.toULong()
    if (u == 0UL) return ALPHABET[0].toString()
    val sb = StringBuilder()
    while (u > 0UL) {
        val rem = (u % BASE.toULong()).toInt()
        sb.append(ALPHABET[rem])
        u /= BASE.toULong()
    }
    return sb.reverse().toString()
}

fun String.fromBase62(): Long {
    require(isNotEmpty()) { "Cannot decode empty string" }
    var u = 0UL
    for (c in this) {
        val idx = ALPHABET.indexOf(c)
        require(idx >= 0) { "Invalid char: $c" }
        u = u * BASE.toULong() + idx.toULong()
    }
    return u.toLong()
}