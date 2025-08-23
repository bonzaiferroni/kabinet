package kabinet.console

enum class LogColor(val foreground: Foreground, val background: Background) {
    Blue("#9CC6F3".toAnsiForeground(), "#183348".toAnsiBackground()),
    Green("#50C878".toAnsiForeground(), "#002912".toAnsiBackground()),
    Brown("#a47243".toAnsiForeground(), "#3d3223".toAnsiBackground()),
    Purple("#b36d8d".toAnsiForeground(), "#1D003C".toAnsiBackground()),
    Yellow("#aaaf59".toAnsiForeground(), "#857042".toAnsiBackground()),
    Red("#FF4500".toAnsiForeground(), "#4d2e31".toAnsiBackground()),
}

internal const val blueFg = "#9CC6F3"
internal const val orangeFg = "#FF4500"
internal const val greenFg = "#50C878"
internal const val purpleFg = "#967BB6"
internal const val yellowFg = "#FFD700"
internal const val cyanFg = "#4AC7C7"

internal const val blueBg = "#183348"
internal const val greenBg = "#002912"
internal const val grayBg = "#1e2c2c"
internal const val brownBg = "#1c1711"

internal fun wrapForeground(value: String, hex: String) = wrapForeground(value, hex.toAnsiForeground())
internal fun wrapForeground(value: String, foreground: Foreground) = "$foreground$value$ansiDefaultForeground"

internal fun wrapBackground(value: String, hex: String) = wrapBackground(value, hex.toAnsiBackground())
internal fun wrapBackground(value: String, background: Background) = "$background$value$ansiDefaultBackground"

internal fun hexToRgb(hex: String): Triple<Int, Int, Int> {
    // Remove the '#' if it be there and convert to an integer
    val color = hex.removePrefix("#").toInt(16)
    // Extract the RGB components
    val red = (color shr 16) and 0xFF
    val green = (color shr 8) and 0xFF
    val blue = color and 0xFF
    // Return as a Triple
    return Triple(red, green, blue)
}

internal fun rgbToXterm256(r: Int, g: Int, b: Int): Int {
    fun to6(x: Int) = when {
        x < 48 -> 0
        x < 114 -> 1
        else -> (x - 35) / 40
    }.coerceIn(0, 5)
    val r6 = to6(r); val g6 = to6(g); val b6 = to6(b)
    return 16 + 36 * r6 + 6 * g6 + b6
}

// fun bg256(r: Int, g: Int, b: Int) = "\u001B[48;5;${rgbToXterm256(r,g,b)}m"

internal fun String.toAnsiForeground(): Foreground {
    val (r, g, b) = hexToRgb(this)
    return Foreground("\u001B[38;2;$r;$g;${b}m")
}

internal fun String.toAnsiBackground(): Background {
    val (r, g, b) = hexToRgb(this)
    return Background("\u001B[48;2;$r;$g;${b}m")
}

internal val escapeChar = Char(27).toString()
internal val ansiDefaultForeground = Foreground("\u001B[39m")
internal val ansiDefaultBackground = Background("\u001B[49m")
internal val ansiBlackForeground = Foreground("\u001B[30m")
internal val ansiRedForeground = Foreground("\u001B[31m")
internal val ansiGreenForeground = Foreground("\u001B[32m")
internal val ansiYellowForeground = Foreground("\u001B[33m")
internal val ansiBlueForeground = Foreground("\u001B[34m")
internal val ansiMagentaForeground = Foreground("\u001B[35m")
internal val ansiCyanForeground = Foreground("\u001B[36m")
internal val ansiWhiteForeground = Foreground("\u001B[37m")
internal val dimForeground = "#999999".toAnsiForeground()
internal val darkForeground = "#555555".toAnsiForeground()
