package kabinet.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDateTimeUtc() = toLocalDateTime(TimeZone.UTC).let { time ->
    LocalDateTime(
        year = time.year,
        monthNumber = time.monthNumber,
        dayOfMonth = time.dayOfMonth,
        hour = time.hour,
        minute = time.minute,
        second = time.second,
        nanosecond = 0
    )
}

fun LocalDateTime.toInstantUtc() = this.toInstant(TimeZone.UTC)

fun Instant.toLocalDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())