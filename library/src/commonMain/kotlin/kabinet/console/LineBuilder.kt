package kabinet.console

class LineBuilder(
) {
    val builder = StringBuilder()
    var foreground = ansiDefaultForeground
    var background = ansiDefaultBackground
    var format = defaultText
    val length get () = builder.length
    val isEmpty get () = builder.isEmpty()

    fun write(part: LogPart) = write(part.toString())

    fun write(part: String): LineBuilder {
        builder.append(part)
        return this
    }

    fun writeLength(message: String, length: Int): LineBuilder {
        val formattedMessage = if (message.length > length) {
            message.take(length)
        } else {
            message.padEnd(length)
        }
        return write(formattedMessage)
    }

    fun setForeground(hex: String) = setForeground(hex.toAnsiForeground())

    fun setForeground(foreground: Foreground): LineBuilder {
        if (this.foreground == foreground) return this
        write(foreground)
        this.foreground = foreground
        return this
    }

    fun setForeground(level: LogLevel): LineBuilder {
        val color = when (level) {
            LogLevel.Trace -> LogColor.Blue.foreground
            LogLevel.Debug -> LogColor.Purple.foreground
            LogLevel.Info -> LogColor.Green.foreground
            LogLevel.Warning -> LogColor.Yellow.foreground
            LogLevel.Error -> LogColor.Red.foreground
        }
        return setForeground(color)
    }

    fun defaultForeground() = setForeground(ansiDefaultForeground)

    fun setBackground(hex: String) = setBackground(hex.toAnsiBackground())

    fun setBackground(background: Background): LineBuilder {
        if (this.background == background) return this
        write(background)
        this.background = background
        return this
    }

    fun defaultBackground() = setBackground(ansiDefaultBackground)

    fun setFormat(format: Format): LineBuilder {
        if (this.format == format) return this
        write(format)
        this.format = format
        return this
    }

    fun defaultFormat() = setFormat(defaultText)
    fun bold() = setFormat(boldText)
    fun underscore() = setFormat(underlineText)
    fun italicize() = setFormat(italicText)

    override fun toString() = builder.toString()

    fun clear(): LineBuilder {
        setForeground(ansiDefaultForeground)
        setBackground(ansiDefaultBackground)
        setFormat(defaultText)
        builder.clear()
        return this
    }

    fun build(): String {
        val string = toString()
        clear()
        return string
    }

    fun current(): String {
        return toString()
    }
}