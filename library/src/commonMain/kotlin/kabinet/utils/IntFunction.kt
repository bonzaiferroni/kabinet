package kabinet.utils

fun Int.toOrdinalSuffix(): String {
    val a = kotlin.math.abs(this.toLong())
    val lastTwo = (a % 100).toInt()
    val last = (a % 10).toInt()
    val suffix = if (lastTwo == 11 || lastTwo == 12 || lastTwo == 13) "th" else when (last) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
    return "$this$suffix"
}