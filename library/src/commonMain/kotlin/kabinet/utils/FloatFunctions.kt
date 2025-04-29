package kabinet.utils

fun Float.toMetricString(): String {
    val abs = kotlin.math.abs(this)
    val (divisor, suffix) = when {
        abs >= 1_000_000_000_000_000 -> 1_000_000_000_000_000f to "Q"
        abs >= 1_000_000_000_000     -> 1_000_000_000_000f     to "T"
        abs >= 1_000_000_000         -> 1_000_000_000f         to "B"
        abs >= 1_000_000             -> 1_000_000f             to "M"
        abs >= 1_000                 -> 1_000f                 to "k"
        else                         -> 1f                     to ""
    }

    val result = this / divisor
    val rounded = (result * 10).toInt() / 10f // 1 decimal place

    val showDecimal = suffix != ""
    return if (showDecimal) "$rounded$suffix" else "${rounded.toInt()}$suffix"
}

fun Double.toMetricString() = this.toFloat().toMetricString()