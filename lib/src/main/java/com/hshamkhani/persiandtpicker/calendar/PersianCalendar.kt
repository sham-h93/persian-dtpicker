package com.hshamkhani.persiandtpicker.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils.dayOfWeek
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

    var daysList by remember(daysInMonth) {
        mutableStateOf(
            (1..daysInMonth).map {
                it.toString().formatToHindiIfLanguageIsFa()
            }
        )
    }

    LaunchedEffect(daysInMonth) {
        val start = simpleDate.copy(day = 1).dayOfWeek()
        val end = simpleDate.copy(day = daysInMonth).dayOfWeek() % 7
        val padStart = List(start) { "" }
        val padEnd = List(end) { "" }
        daysList = padStart + daysList + padEnd
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
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
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
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(onClick = {}),
                    text = day,
                    style = textStyle
                )
            }
        }
    }
}