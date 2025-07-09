package kabinet

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

val PAST_MOMENT = LocalDate.parse("2017-03-20").atStartOfDayIn(TimeZone.UTC)