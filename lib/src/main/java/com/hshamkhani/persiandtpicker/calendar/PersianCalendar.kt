package com.hshamkhani.persiandtpicker.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.SimpleDate

@Composable
fun PersianCalendar(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val isEng = Locale.current.language != "fa"
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(initialDate) }

    val years by remember {
        mutableStateOf(
            DatePickerUtils.initYearList(initialDate.year)
                .map { it.toString().formatToHindiIfLanguageIsFa() })
    }

    val months by remember {
        mutableStateOf(
            DatePickerUtils.initMonthList(isEng = isEng)
        )
    }

    val daysInMonth by remember(simpleDate.month) {
        mutableStateOf(
            DatePickerUtils.monthLength(simpleDate.month, simpleDate.year).size
        )
    }

    // Calculate the starting weekday of the month (0 = Saturday, 6 = Friday)
    val firstDayOfMonthWeekday by remember(simpleDate.month, simpleDate.year) {
        mutableStateOf(
            calculateFirstDayOfMonthWeekday(simpleDate.month, simpleDate.year)
        )
    }

    // Create list with empty strings for days before the 1st
    val daysList by remember(daysInMonth, firstDayOfMonthWeekday) {
        mutableStateOf(
            List(firstDayOfMonthWeekday) { "" } + (1..daysInMonth)
                .map {
                    it.toString().formatToHindiIfLanguageIsFa()
                }
        )
    }

    Column(
        modifier = modifier
    ) {
        // Weekday headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DatePickerUtils.weekDays().forEach { weekDay ->
                Text(
                    text = weekDay.take(3),
                    style = textStyle
                )
            }
        }

        // Calendar days
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            maxItemsInEachRow = 7
        ) {
            daysList.forEach { day ->
                Text(
                    text = day,
                    style = textStyle
                )
            }
        }
    }
}

// Function to calculate the weekday of the first day of the month
// Returns 0 for Saturday, 1 for Sunday, ..., 6 for Friday
fun calculateFirstDayOfMonthWeekday(month: Int, year: Int): Int {
    // This is a simplified implementation - you'll need to replace with actual Persian calendar logic
    // For now, using a placeholder that assumes a fixed pattern
    val baseDay = (year * 12 + month) % 7 // This is just an example
    return (baseDay + 1) % 7 // Adjust based on your calendar's starting day
}