package kabinet.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

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

fun Instant.toLocalDateTime() = this.toLocalDateTime(TimeZone.currentSystemDefault())

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

fun Instant.toTimeFormat() = this.toLocalDateTime().let { "${it.hour12}:${it.minute.toString().padStart(2, '0')} ${it.amPmLabel}" }

fun Instant.toRelativeTimeFormat () = "${this.toRelativeDayFormat()} at ${this.toTimeFormat()}"

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
    else -> error("Not a day of the week")
}

fun DayOfWeek.toLongFormat() = when (this) {
    DayOfWeek.MONDAY -> "Monday"
    DayOfWeek.TUESDAY -> "Tuesday"
    DayOfWeek.WEDNESDAY -> "Wednesday"
    DayOfWeek.THURSDAY -> "Thursday"
    DayOfWeek.FRIDAY -> "Friday"
    DayOfWeek.SATURDAY -> "Saturday"
    DayOfWeek.SUNDAY -> "Sunday"
    else -> error("Not a day of the week")
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
    else -> error("Not a month")
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
    else -> error("Not a month")
}