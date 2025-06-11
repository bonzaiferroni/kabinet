package kabinet.utils

fun String.obfuscate(): String {
    return this.map { it.code.xor('s'.code).toChar() }.joinToString("")
}

fun String.deobfuscate(): String {
    return this.map { it.code.xor('s'.code).toChar() }.joinToString("")
}

fun String.takeEllipsis(length: Int, ellipsis: String = "..."): String =
    if (this.length > length) take(length) + ellipsis else this

fun pluralize(value: Int) = if (value == 1) "" else "s"