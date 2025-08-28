package kabinet.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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

fun LocalDateTime.toInstantFromUtc() = this.toInstant(TimeZone.UTC)

fun LocalDateTime.toInstantFromLocal() = this.toInstant(TimeZone.currentSystemDefault())

fun Instant.toLocalDateTime(isLocalTime: Boolean = false) = this.toLocalDateTime(if (isLocalTime) TimeZone.UTC else TimeZone.currentSystemDefault())

fun Instant.toRelativeDayFormat(): String {
    val time = this.toLocalDateTime()
    val now = Clock.System.now()
    val today = now.toLocalDateTime()
    if (time.date == today.date) return "Today"
    val tomorrow = (now + 1.days).toLocalDateTime()
    if (time.date == tomorrow.date) return "Tomorrow"
    val nextWeek = (now + 7.days).toLocalDateTime()
    if (time < nextWeek) return time.dayOfWeek.toLongFormat()
    val nextYear = (now + 365.days).toLocalDateTime()
    if (time < nextYear) return "${time.dayOfWeek.toShortFormat()}, ${time.month.toShortFormat()}. ${time.dayOfMonth}"
    else return "${time.monthNumber}/${time.dayOfMonth}/${time.year}"
}

fun Instant.toTimeFormat(isLocalTime: Boolean = false) = this.toLocalDateTime(isLocalTime)
    .let { "${it.hour12}:${it.minute.toString().padStart(2, '0')} ${it.amPmLabel}" }

fun Instant.toRelativeTimeFormat (isLocalTime: Boolean = false) = "${this.toRelativeDayFormat()} at ${this.toTimeFormat(isLocalTime)}"

val LocalDateTime.hour12 get() = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour

val LocalDateTime.isPm get() = hour >= 12

val LocalDateTime.amPmLabel get() = if (isPm) "PM" else "AM"

fun DayOfWeek.toShortFormat() = when (this) {
    DayOfWeek.MONDAY -> "Mon"
    DayOfWeek.TUESDAY -> "Tue"
    DayOfWeek.WEDNESDAY -> "Wed"
    DayOfWeek.THURSDAY -> "Thu"
    DayOfWeek.FRIDAY -> "Fri"
    DayOfWeek.SATURDAY -> "Sat"
    DayOfWeek.SUNDAY -> "Sun"
}

fun DayOfWeek.toLongFormat() = when (this) {
    DayOfWeek.MONDAY -> "Monday"
    DayOfWeek.TUESDAY -> "Tuesday"
    DayOfWeek.WEDNESDAY -> "Wednesday"
    DayOfWeek.THURSDAY -> "Thursday"
    DayOfWeek.FRIDAY -> "Friday"
    DayOfWeek.SATURDAY -> "Saturday"
    DayOfWeek.SUNDAY -> "Sunday"
}

fun Month.toShortFormat() = when (this) {
    Month.JANUARY -> "Jan"
    Month.FEBRUARY -> "Feb"
    Month.MARCH -> "Mar"
    Month.APRIL -> "Apr"
    Month.MAY -> "May"
    Month.JUNE -> "Jun"
    Month.JULY -> "Jul"
    Month.AUGUST -> "Aug"
    Month.SEPTEMBER -> "Sep"
    Month.OCTOBER -> "Oct"
    Month.NOVEMBER -> "Nov"
    Month.DECEMBER -> "Dec"
}

fun Month.toLongFormat() = when (this) {
    Month.JANUARY -> "January"
    Month.FEBRUARY -> "February"
    Month.MARCH -> "March"
    Month.APRIL -> "April"
    Month.MAY -> "May"
    Month.JUNE -> "June"
    Month.JULY -> "July"
    Month.AUGUST -> "August"
    Month.SEPTEMBER -> "September"
    Month.OCTOBER -> "October"
    Month.NOVEMBER -> "November"
    Month.DECEMBER -> "December"
}

fun Instant.toDoubleMillis(): Double = toEpochMilliseconds().toDouble()
fun Instant.Companion.fromDoubleMillis(d: Double): Instant =
    Instant.fromEpochMilliseconds(d.toLong())

fun Instant.toTimeDescription() = this.toLocalDateTime().let { "${it.hour12}:${it.minute.toString().padStart(2, '0')} ${it.amPmLabel}" }
fun Instant.toDayDescription() = this.toLocalDateTime().let { "${it.dayOfWeek.toLongFormat()} the ${it.dayOfMonth.toOrdinalSuffix()} of ${it.month.toLongFormat()}" }

fun Instant.Companion.fromEpochSecondsDouble(value: Double) = Instant.fromEpochSeconds(value.toLong())

private val REL_RE = Regex("""^\s*(\d+)\s+(second|minute|hour|day|month|year)s?\s+ago\s*$""", RegexOption.IGNORE_CASE)

fun Instant.Companion.fromAgoFormat(input: String, zone: TimeZone = TimeZone.currentSystemDefault()): Instant? {
    val match = REL_RE.matchEntire(input) ?: return null
    val amount = match.groupValues[1].toLong()
    val unit = match.groupValues[2].lowercase()
    val now = Clock.System.now()

    return when (unit) {
        "second" -> now - amount.seconds
        "minute" -> now - amount.minutes
        "hour"   -> now - amount.hours
        "day"    -> now - amount.days
        "month"  -> now.minus(amount, DateTimeUnit.MONTH, zone)
        "year"   -> now.minus(amount, DateTimeUnit.YEAR, zone)
        else     -> null
    }
}