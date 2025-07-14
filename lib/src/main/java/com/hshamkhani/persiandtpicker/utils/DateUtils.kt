package com.hshamkhani.persiandtpicker.utils

import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.padZeroToStartWithPersianDigits

internal object DateUtils {

    fun initYearList(shYear: Int): List<Int> {
        val values = mutableListOf<Int>()
        (shYear - 1 until shYear + 20).map { year ->
            values.add(year)
        }
        return values
    }

    fun initMonthList(isEng: Boolean): List<String> {
        val values: MutableList<String>
        repeat(12) {
        }
        if (isEng) {
            values = mutableListOf(
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
        } else {
            values = mutableListOf(
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

    fun initHours(is24h: Boolean): List<String> {
        val values = mutableListOf<String>()
        val repeatValue = if (is24h) 24 else 12
        repeat(repeatValue) { index ->
            val hour = if (is24h) index else index + 1
            values.add(hour.padZeroToStartWithPersianDigits())
        }
        return values
    }

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

}