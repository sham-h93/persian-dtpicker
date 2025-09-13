package com.hshamkhani.persiandtpicker.utils

import android.content.Context
import android.text.format.DateFormat
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils.gregorianToJalali
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

/**
 *  Represents a simple date (either gregorian or jalali)
 *  with year, month, day, and an optional time.
 *
 *  * `year`: The year of the date (e.g., 1402 for Jalali, 2023 for Gregorian).
 *  * `month`: The month of the date (1-12 for both Jalali and Gregorian).
 *  * `day`: The day of the month (1-31 depending on the month).
 *  * `time`: An `SimpleTime` object representing the time of the day.
 *  */
data class SimpleDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val time: SimpleTime = SimpleTime()
) {

    companion object {
        /**
         * Represents current jalali date and time as a `SimpleDate` object.
         * */
        fun now(context: Context): SimpleDate {
            val date = DatePickerUtils.currentJalaliDate()
            val dateTime = LocalDateTime.now(ZoneId.systemDefault())
            val is24Hour = DateFormat.is24HourFormat(context)

            return SimpleDate(
                year = date.component1(),
                month = date.component2(),
                day = date.component3(),
                time = SimpleTime(
                    hour = dateTime.hour,
                    minute = dateTime.minute,
                    clockPeriod = if (is24Hour) null else {
                        if (dateTime.hour < 12) ClockPeriod.Am else ClockPeriod.Pm
                    }
                ),
            )

        }

        /**
         * This function converts a timestamp (in milliseconds) to a jalali `SimpleDate` object.
         *
         * * `timestamp`: The timestamp in milliseconds.
         * */
        fun from(timestamp: Long, context: Context): SimpleDate {
            val localDateTime = Date(timestamp).let { date ->
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(date.time),
                    ZoneId.systemDefault()
                )
            }

            val jalaliDt = gregorianToJalali(
                gy = localDateTime.year,
                gm = localDateTime.monthValue,
                gd = localDateTime.dayOfMonth
            )

            val dateTime = LocalDateTime.now(ZoneId.systemDefault())
            val is24Hour = DateFormat.is24HourFormat(context)

            return SimpleDate(
                year = jalaliDt.component1(),
                month = jalaliDt.component2(),
                day = jalaliDt.component3(),
                time = SimpleTime(
                    hour = dateTime.hour,
                    minute = dateTime.minute,
                    clockPeriod = if (is24Hour) null else {
                        if (dateTime.hour < 12) ClockPeriod.Am else ClockPeriod.Pm
                    }
                )
            )
        }
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
 *  * `hour`: hour The hour of the time (0-23 for 24-hour format,
 *  1-12 for 12-hour format).
 *  * `minute`: minute The minute of the time (0-59).
 *  * `clockPeriod`: An optional `ClockPeriod` enum value that indicates that the time is in 12-hour format.
 * */
data class SimpleTime(
    val hour: Int = 0,
    val minute: Int = 0,
    val clockPeriod: ClockPeriod? = null
) {
    companion object {
        /**
         * This function returns the current time as a `SimpleTime` object.
         * */
        fun now(context: Context): SimpleTime {
            val time = LocalTime.now(ZoneId.systemDefault())
            val is24Hour = DateFormat.is24HourFormat(context)

            return SimpleTime(
                hour = time.hour,
                minute = time.minute,
                clockPeriod = if (is24Hour) null else {
                    if (time.hour < 12) ClockPeriod.Am else ClockPeriod.Pm
                }
            )
        }
    }
}


/**
 * This function converts a Jalali `SimpleDate` to a Gregorian `SimpleDate`.
 *
 * @param year The Jalali year.
 * @param month The Jalali month.
 * @param day The Jalali day.
 *
 * @return A SimpleDate object representing the Gregorian date.
 * */
fun SimpleDate.totGregorianDate(): SimpleDate {
    val (year, month, day) = DatePickerUtils.jalaliToGregorian(
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

/**
 * This function converts a Gregorian `SimpleDate` to a Calendar.
 *
 * @param this The SimpleDate object representing the Gregorian date.
 * @return A Calendar object.
 * */

fun SimpleDate.toCalendar(): Calendar {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // Calendar months are 0-based
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.MINUTE, this@toCalendar.time.minute)
        if (this@toCalendar.time.clockPeriod != null) {
            set(
                Calendar.AM_PM,
                if (this@toCalendar.time.clockPeriod == ClockPeriod.Am) Calendar.AM else Calendar.PM
            )
            set(Calendar.HOUR, this@toCalendar.time.hour)
        } else {
            set(Calendar.HOUR_OF_DAY, this@toCalendar.time.hour)
        }
    }
    return calendar
}

