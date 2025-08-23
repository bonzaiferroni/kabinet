package kabinet.console

import kotlin.reflect.KClass

class LogConsole(
    val minLevel: LogLevel = LogLevel.Debug,
    val writer: LogWriter? = null
) {
    private val builder = LineBuilder()
    private var lastSource: String? = null

    fun <T: Any> getHandle(kClass: KClass<T>) = getHandle(kClass.simpleName ?: error("class name not found"))

    fun getHandle(name: String, level: LogLevel = LogLevel.Info): LogHandle {
        val handle = LogHandle(name, level, this)
        return handle
    }

    fun log(message: String) = log("", LogLevel.Info, message)

    fun log(source: String, level: LogLevel, message: Any?) {
        val msg = message.toString()
        writer?.let { writer ->
            if (level.ordinal >= writer.minLevel.ordinal) writer.writeLine(source, level, msg)
        }

        if (level.ordinal < minLevel.ordinal) return

        val displayedSource = if (lastSource == source) "" else source
        lastSource = source

        val line = builder.bold().setForeground(level).writeLength(displayedSource, MAX_SOURCE_CHARS)
            .defaultFormat().defaultForeground().write(" ").write(msg).build()
        println(line)
    }
}

const val MAX_SOURCE_CHARS = 8

class LogHandle(
    val name: String,
    val level: LogLevel = LogLevel.Info,
    private val console: LogConsole,
) {
    fun log(message: Any?, level: LogLevel = LogLevel.Info) {
        console.log(name, level, message)
    }
}