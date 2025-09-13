package com.hshamkhani.persiandtpicker.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils.dayOfWeek
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.SimpleDate

@Composable
fun PersianCalendar(
    modifier: Modifier = Modifier,
    dayAlign: Alignment = Alignment.Center,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    val isEng = Locale.current.language != "fa"
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(selectedDate ?: initialDate) }

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
        val end = 7 - simpleDate.copy(day = daysInMonth).dayOfWeek()
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
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DatePickerUtils.weekDays().forEach { weekDay ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = weekDay.take(3).uppercase(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily,
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
                val isDay =  day.isNotBlank()
                val isCurrentDay = isDay && day.toInt() == simpleDate.day
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(
                            if (isCurrentDay) {
                                selectedItemBackgroundColor
                            } else {
                                backGroundColor
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCurrentDay) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Transparent
                            },
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable(onClick = {
                            if (isDay) {
                                onDateSelected(simpleDate.copy(day = day.toInt()))
                            }
                        }),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(dayAlign),
                        text = day,
                        fontFamily = fontFamily,
                        style = textStyle,
                        color = if (isCurrentDay) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                    )
                }
            }
        }
    }
}