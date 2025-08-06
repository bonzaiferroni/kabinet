package kabinet.utils

import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

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

    val result  = this / divisor
    val rounded = (result * 10).toInt() / 10f

    return if (suffix.isNotEmpty()) {
        "$rounded$suffix"
    } else {
        if (rounded < 10 && rounded % 1f != 0f) {
            "$rounded"
        } else {
            "${rounded.toInt()}"
        }
    }
}

fun Double.toMetricString() = this.toFloat().toMetricString()

fun Float.Companion.random() = Random.nextFloat()

fun Float.Companion.random(max: Float) = random() * max

fun Float.Companion.random(min: Float, maxFloat: Float) = random() * (maxFloat - min) + min

fun Float.format(decimals: Int = 2): String {
    val multiplier = 10.0.pow(decimals).toFloat()
    return (round(this * multiplier) / multiplier).toString()
}

fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t