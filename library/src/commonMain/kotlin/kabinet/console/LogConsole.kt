package kabinet.console

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.KClass

class LogConsole(
    val minLevel: LogLevel = LogLevel.Debug,
    val writer: LogWriter? = null
) {
    private var lastSource: String? = null

    fun <T: Any> getHandle(kClass: KClass<T>) = getHandle(kClass.simpleName ?: error("class name not found"))

    fun getHandle(name: String, level: LogLevel = LogLevel.Info): LogHandle {
        val handle = LogHandle(name, level, this)
        return handle
    }

    fun log(message: String) = log("", LogLevel.Info, message)

    fun log(source: String, level: LogLevel, message: Any?) {
        val msg = message.toString()

        writer?.let { w ->
            if (level.ordinal >= w.minLevel.ordinal) w.writeLine(source, level, msg)
        }
        if (level.ordinal < minLevel.ordinal) return

        val displayedSource = if (lastSource == source) "" else source
        lastSource = source

        val line = LineBuilder()
            .bold().setForeground(level).writeLength(displayedSource, MAX_SOURCE_CHARS)
            .defaultFormat().defaultForeground().write(" ").write(msg)
            .build()

        println(line)
    }
}

const val MAX_SOURCE_CHARS = 8

// val globalConsole = LogConsole()

@Deprecated("use KotlinLogging.logger")
object globalConsole {
    fun getHandle(kClass: KClass<*>) = LogWrapper(kClass.simpleName!!)
    fun getHandle(source: String) = LogWrapper(source)
}

class LogWrapper(
    name: String
) {
    private val logger = KotlinLogging.logger(name)

    fun info(message: () -> String) = logger.info(message)
    fun info(message: String) = logger.info { message }
    fun log(message: String) = logger.info { message }
    fun error(e: Throwable, message: () -> String) = logger.error(e, message)
    fun error(message: String) = logger.error { message }
    fun error(e: Throwable) = logger.error(e) { }
    fun warn(message: String) = logger.warn { message }
}