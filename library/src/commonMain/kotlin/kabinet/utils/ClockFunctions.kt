package kabinet.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

fun Clock.Companion.epochSecondsNow() = Clock.System.now().epochSeconds

fun Clock.Companion.startOfDay() = Clock.System.now().toLocalDateTime(tz).date.atStartOfDayIn(tz)

fun Clock.Companion.today() = Clock.System.now().toLocalDateTime(tz).date

fun Clock.Companion.startOfWeek(): Instant {
    val now =Clock.System.now()
    val time = now.toLocalDateTime()
    val dayOfWeek = time.dayOfWeek.ordinal
    return (now - dayOfWeek.days).toLocalDateTime().date.atStartOfDayIn(tz)
}

fun Clock.Companion.nowToLocalDateTimeUtc() = Clock.System.now().toLocalDateTimeUtc()

private val tz = TimeZone.currentSystemDefault()

fun Instant.Companion.tryParse(str: String) = try {
    parse(str)
} catch (e: Exception) {
    try {
        LocalDateTime.parse(str).toInstant(TimeZone.UTC)
    } catch (_: Exception) {
        null
    }
}

fun String.tryParseInstantOrNull() = Instant.tryParse(this)