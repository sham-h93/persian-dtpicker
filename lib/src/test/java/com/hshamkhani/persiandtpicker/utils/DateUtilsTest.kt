package com.hshamkhani.persiandtpicker.utils

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    @Test
    fun isLeap_matchesOfficialCalendar() {
        listOf(1395, 1399, 1403, 1408, 1412).forEach { assertEquals("$it", true, it.isLeap()) }
        listOf(1400, 1402, 1404, 1407, 1411).forEach { assertEquals("$it", false, it.isLeap()) }
    }

    @Test
    fun lastDayOfEveryYear_isFollowedByFarvardinFirst() {
        for (year in 1250..1550) {
            val lastDay = SimpleDate(year, 12, jalaliMonthLength(12, year)).totGregorianDate()
            val (gy, gm, gd) = jalaliToGregorian(year + 1, 1, 1)
            val nextDay = java.time.LocalDate.of(lastDay.year, lastDay.month, lastDay.day).plusDays(1)
            assertEquals("$year", java.time.LocalDate.of(gy, gm, gd), nextDay)
        }
    }

    @Test
    fun everyJalaliDay_roundTripsThroughGregorian() {
        for (year in 1250..1550) {
            for (month in 1..12) {
                for (day in 1..jalaliMonthLength(month, year)) {
                    val (gy, gm, gd) = jalaliToGregorian(year, month, day)
                    assertArrayEquals(
                        "$year/$month/$day",
                        intArrayOf(year, month, day),
                        gregorianToJalali(gy, gm, gd),
                    )
                }
            }
        }
    }

    @Test
    fun knownDates_convertCorrectly() {
        assertArrayEquals(intArrayOf(1403, 1, 1), gregorianToJalali(2024, 3, 20))
        assertArrayEquals(intArrayOf(1403, 12, 30), gregorianToJalali(2025, 3, 20))
        assertArrayEquals(intArrayOf(1404, 1, 1), gregorianToJalali(2025, 3, 21))
        assertArrayEquals(intArrayOf(1405, 7, 26), gregorianToJalali(2026, 10, 18))
    }

    @Test
    fun simpleTime_convertsBetween12And24Hour() {
        assertEquals(SimpleTime(12, 5, ClockPeriod.Am), SimpleTime.of(0, 5, is24Hour = false))
        assertEquals(SimpleTime(12, 5, ClockPeriod.Pm), SimpleTime.of(12, 5, is24Hour = false))
        assertEquals(SimpleTime(9, 0, ClockPeriod.Pm), SimpleTime.of(21, 0, is24Hour = false))
        assertEquals(SimpleTime(21, 0), SimpleTime.of(21, 0, is24Hour = true))
        for (h in 0..23) {
            assertEquals(h, SimpleTime.of(h, 0, is24Hour = false).hourOfDay())
            assertEquals(h, SimpleTime.of(h, 0, is24Hour = true).hourOfDay())
        }
    }

    @Test
    fun toCalendar_keepsNoonOnTheSameDay() {
        val calendar = SimpleDate(2026, 10, 18, SimpleTime(12, 30, ClockPeriod.Pm)).toCalendar()
        assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, calendar.get(Calendar.MINUTE))
    }

    @Test
    fun plusDays_crossesMonthAndYear() {
        assertEquals(SimpleDate(1404, 1, 1), SimpleDate(1403, 12, 30).plusDays(1))
        assertEquals(SimpleDate(1403, 12, 30), SimpleDate(1404, 1, 1).plusDays(-1))
    }
}
