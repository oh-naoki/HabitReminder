package extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Instant.startOfDay(): Instant {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    val startOfDay = LocalDateTime(localDateTime.date, LocalTime(0, 0))
    return startOfDay.toInstant(TimeZone.currentSystemDefault())
}

fun Instant.endOfDay(): Instant {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    val endOfDay = LocalDateTime(localDateTime.date, LocalTime(23, 59))
    return endOfDay.toInstant(TimeZone.currentSystemDefault())
}
