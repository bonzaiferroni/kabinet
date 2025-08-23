package kabinet.console



class LogHandle(
    val name: String,
    val level: LogLevel = LogLevel.Info,
    private val console: LogConsole,
) {
    var partialLine = LineBuilder()

    fun log(message: Any?, level: LogLevel = LogLevel.Info) {
        console.log(name, level, message)
    }

    fun logTrace(message: String) = log(message, LogLevel.Trace)
    fun logDebug(message: String) = log(message, LogLevel.Debug)
    fun logInfo(message: String) = log(message, LogLevel.Info)
    fun logWarning(message: String) = log(message, LogLevel.Warning)
    fun logError(message: String) = log(message, LogLevel.Error)

    fun logPartial(message: String) {
        if (partialLine.isEmpty) partialLine.write(message)
        else partialLine.setForeground(darkForeground).write("┃").defaultForeground().write(message)
    }

    fun cell(
        value: Any,
        width: Int? = null,
        color: LogColor? = null,
        label: String? = null,
        justify: LogJustify = LogJustify.RIGHT,
        isValid: (() -> Boolean?)? = null
    ): LogHandle {
        val showValue = isValid == null || isValid() == true
        val rawContent = if (showValue) value.toString() else "💢"
        val result = width?.let {
            val labelWidth = label?.let { minOf(width - rawContent.length - 1, label.length) } ?: 0
            val contentWidth = labelWidth.takeIf{ labelWidth > 0 }?.let { width - labelWidth - 1 } ?: width
            val contentPart = if (justify == LogJustify.RIGHT) {
                rawContent.padStart(contentWidth).takeLast(contentWidth)
            } else {
                rawContent.padEnd(contentWidth).take(contentWidth)
            }.let { if (color != null) wrapForeground(it, color.foreground) else it }
            if (labelWidth > 0 && label != null) {
                val labelPart = label.take(labelWidth)
                if (justify == LogJustify.RIGHT) {
                    "${wrapForeground(labelPart, darkForeground)} $contentPart"
                } else {
                    "$contentPart ${wrapForeground(labelPart, darkForeground)}"
                }
            } else {
                contentPart
            }
        } ?: color?.let { wrapForeground(rawContent, it.foreground) } ?: rawContent
        logPartial(result)
        return this
    }

    fun send(
        message: String? = null,
        level: LogLevel = LogLevel.Info,
        background: LogColor? = null,
        width: Int? = null,
    ) {
        message?.let { logPartial(it) }
        var line = partialLine.build().let { line ->
            if (width == null) return@let line
            val displayLength = line.visibleLength()
            if (displayLength >= width) return@let line
            return@let "$line${"".padEnd(width - displayLength, ' ')}"
        }
        background?.let{ line = wrapBackground(line, it.background) }
        console.log(name, level, line)
    }
}

enum class LogJustify {
    RIGHT,
    LEFT,
}

private fun String.visibleLength(): Int {
    var length = 0
    var inAnsiCode = false

    for (char in this) {
        if (char == '\u001B') {
            inAnsiCode = true // Start of an ANSI escape sequence
        } else if (inAnsiCode && char == 'm') {
            inAnsiCode = false // End of an ANSI escape sequence
        } else if (!inAnsiCode) {
            length++ // Count the character if not in an ANSI code
        }
    }

    return length
}