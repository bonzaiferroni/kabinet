package kabinet.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Clock.Companion.epochSecondsNow() = Clock.System.now().epochSeconds

fun Clock.Companion.startOfDay() = Clock.System.now().toLocalDateTime(tz).date.atStartOfDayIn(tz)

fun Clock.Companion.nowToLocalDateTimeUtc() = Clock.System.now().toLocalDateTimeUtc()

private val tz = TimeZone.currentSystemDefault()
