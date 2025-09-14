package com.hshamkhani.persiandtpicker.utils

import android.icu.util.Calendar
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.padZeroToStartWithPersianDigits
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale

internal object DatePickerUtils {

    private val englishWeekDays = listOf(
        "Saturday",
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
    )

    private val persianWeekDays = listOf(
        "شنیه",
        "یک\u200Cشنبه",
        "دوشنبه",
        "سه\u200Cشنبه",
        "چهارشنبه",
        "پتج\u200Cشنبه",
        "جمعه",
    )

    private val farsiMonthNames = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )

    private val englishMonthNames = listOf(
        "Farvardin",
        "Ordibehesht",
        "Khordad",
        "Tir",
        "Mordad",
        "Shahrivar",
        "Mehr",
        "Aban",
        "Azar",
        "Day",
        "Bahman",
        "Esfand"
    )

    fun weekDays() = if (Locale.getDefault().language == "fa") persianWeekDays else englishWeekDays

    fun SimpleDate.dayOfWeek(): Int {
        val day = totGregorianDate().toCalendar().get(Calendar.DAY_OF_WEEK)
        return when (day) {
            Calendar.SATURDAY -> 1
            Calendar.SUNDAY -> 2
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 4
            Calendar.WEDNESDAY -> 5
            Calendar.THURSDAY -> 6
            Calendar.FRIDAY -> 7
            else -> 0
        }
    }

    fun initHours(is24h: Boolean): List<String> {
        val values = mutableListOf<String>()
        val repeatValue = if (is24h) 24 else 12
        repeat(repeatValue) { index ->
            val hour = if (is24h) index else index + 1
            values.add(hour.padZeroToStartWithPersianDigits())
        }
        return values
    }

    fun initDaysList(monthLength: Int): List<String> {
        val values = mutableListOf<String>()
        repeat(monthLength) { monthDay ->
            values.add((monthDay + 1).padZeroToStartWithPersianDigits())
        }
        return values
    }

    /**
     * Initializes a list of years starting from the given year.
     * The list will contain 20 years starting from the given year.
     *
     * @param shYear The starting year in Jalali calendar.
     * @return A list of integers representing the years.
     */
    fun initYearList(shYear: Int): List<Int> {
        val values = mutableListOf<Int>()
        (shYear until shYear + 20).map { year ->
            values.add(year)
        }
        return values
    }

    fun initMonthList(isEng: Boolean): List<String> {
        val values: List<String>
        repeat(12) {
        }
        if (isEng) {
            values = englishMonthNames
        } else {
            values = farsiMonthNames
        }
        return values
    }


    /**
     * Initial jalali date from current date.
     *
     * Returns an IntArray with three elements:
     * 1st element is the year, 2nd element is the month,
     * and 3rd element is the day.
     * */
    fun currentJalaliDate() = LocalDate.now().let { now ->
        gregorianToJalali(
            gy = now.year,
            gm = now.monthValue,
            gd = now.dayOfMonth
        )
    }

    /**
     * Returns the current time in the system's default time zone.
     * */
    fun currentDate() = LocalTime.now(ZoneId.systemDefault() ?: ZoneId.of("UTC"))

    fun initMinutes(): List<String> {
        val values = mutableListOf<String>()
        repeat(60) { index ->
            val minute = index
            values.add(minute.padZeroToStartWithPersianDigits())
        }
        return values
    }

    fun initAmPm(isEng: Boolean): Pair<String, String> {
        return if (isEng) "AM" to "PM" else "ق.ظ" to "ب.ظ"
    }

    private val leapYears = listOf(
        1403,
        1407,
        1411,
        1415,
        1419,
        1423,
        1427,
        1431,
        1435,
        1439
    )

    /**
     * Returns true if the given jalali year is a leap year.
     * */
    fun Int.isLeap(): Boolean = this in leapYears

    /**
     * Returns a list of integers representing the days in a month
     * */

    fun monthLength(monthNumber: Int, year: Int): List<Int> {
        val days29 = (1..29).map { it }
        val days30 = (1..30).map { it }
        val days31 = (1..31).map { it }

        return when (monthNumber) {
            in 1..6 -> days31
            in 7..11 -> days30
            12 -> if (year.isLeap()) days30 else days29
            else -> emptyList()
        }

    }

    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): IntArray {
        val g_d_m: IntArray = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2: Int = if (gm > 2) (gy + 1) else gy
        var days: Int =
            355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + g_d_m[gm - 1]
        var jy: Int = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += ((days - 1) / 365)
            days = (days - 1) % 365
        }
        var jm: Int;
        var jd: Int;
        if (days < 186) {
            jm = 1 + (days / 31)
            jd = 1 + (days % 31)
        } else {
            jm = 7 + ((days - 186) / 30)
            jd = 1 + ((days - 186) % 30)
        }
        return intArrayOf(jy, jm, jd)
    }

    /**
     * Returns the Gregorian date corresponding to the given Jalali date.
     * */

    fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): IntArray {
        val jy1: Int = jy + 1595
        var days: Int =
            -355668 + (365 * jy1) + ((jy1 / 33) * 8) + (((jy1 % 33) + 3) / 4) + jd + (if (jm < 7) ((jm - 1) * 31) else (((jm - 7) * 30) + 186))
        var gy: Int = 400 * (days / 146097)
        days %= 146097
        if (days > 36524) {
            gy += 100 * (--days / 36524)
            days %= 36524
            if (days >= 365) days++
        }
        gy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            gy += ((days - 1) / 365)
            days = (days - 1) % 365
        }
        var gd: Int = days + 1
        val sal_a: IntArray = intArrayOf(
            0,
            31,
            if ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) 29 else 28,
            31,
            30,
            31,
            30,
            31,
            31,
            30,
            31,
            30,
            31
        )
        var gm = 0
        while (gm < 13 && gd > sal_a[gm]) gd -= sal_a[gm++]
        return intArrayOf(gy, gm, gd)
    }


}