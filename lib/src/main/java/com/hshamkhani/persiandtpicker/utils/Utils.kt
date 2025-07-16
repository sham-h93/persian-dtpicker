package com.hshamkhani.persiandtpicker.utils

import java.time.LocalDate

/**
 *  Represents a simple date (either gregorian or jalali)
 *  with year, month, day, and an optional time.
 * */
data class SimpleDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val time: SimpleTime = SimpleTime()
) {
    /**
     * Returns the timestamp in seconds (for gregorian date) since epoch for this date and time.
     */
    fun toTimestampSeconds(): Long {
        val localDate = LocalDate.of(year, month, day)
        val epochSeconds = localDate.toEpochDay() * 86400

        val hour = if (time.clockPeriod != null) {
            if (time.clockPeriod == ClockPeriod.Pm) time.hour + 12 else time.hour
        } else {
            time.hour
        }

        val secondsOfDay = (hour * 3600L) + (time.minute * 60L)

        return epochSeconds + secondsOfDay
    }

    override fun toString(): String {
        return "${year}-${month}-${day}" +
                " ${time.hour}:${time.minute} ${time.clockPeriod ?: ""}"
    }
}

/**
 *  Represents a time with hour, minute, second, and an clock period (AM/PM)
 *  if the clock in 12-hour time format.
 *
 *  * @property hour The hour of the time (0-23 for 24-hour format,
 *  1-12 for 12-hour format).
 *  * @property minute The minute of the time (0-59).
 *  * @property clockPeriod The clock period (AM/PM) if the time is in 12-hour format
 *  otherwise it will be null.
 * */
data class SimpleTime(
    val hour: Int = 0,
    val minute: Int = 0,
    val clockPeriod: ClockPeriod? = null
)

/**
 * Clock period (Am, Pm) for a clock in 12-hour time format.
 * */
enum class ClockPeriod {
    Am, Pm
}

/**
 * This function converts a Jalali date to a Gregorian date.
 *
 * @param year The Jalali year.
 * @param month The Jalali month.
 * @param day The Jalali day.
 *
 * @return A SimpleDate object representing the Gregorian date.
 * */
fun SimpleDate.totGregorianDate(): SimpleDate {
    val (year, month, day) = DateUtils.jalaliToGregorian(
        jy = year,
        jm = month,
        jd = day
    )
    return SimpleDate(
        year = year,
        month = month,
        day = day,
        time = time
    )
}