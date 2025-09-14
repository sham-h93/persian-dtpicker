package com.hshamkhani.persiandtpicker.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hshamkhani.persiandtpicker.components.FlatIconButton
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils.dayOfWeek
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.asStringMonthName
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.padZeroToStartWithPersianDigits
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
    dayContent: @Composable BoxScope.(String, Boolean) -> Unit = { day, isSelected -> },
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(selectedDate ?: initialDate) }

    val daysInMonth by remember(simpleDate.month) {
        mutableIntStateOf(
            DatePickerUtils.initMonthDays(
                monthNumber = simpleDate.month,
                year = simpleDate.year
            ).size
        )
    }

    val daysList = remember(daysInMonth) {
        derivedStateOf {
            val start = simpleDate.copy(day = 1).dayOfWeek() - 1
            val end = 7 - simpleDate.copy(day = daysInMonth).dayOfWeek()

            val monthDays = (1..daysInMonth).map {
                it.toString().formatToHindiIfLanguageIsFa()
            }
            val padStart = List(start) { "" }
            val padEnd = List(end) { "" }

            padStart + monthDays + padEnd
        }
    }

    LaunchedEffect(simpleDate) {
        onDateSelected(simpleDate)
    }

    Column(
        modifier = modifier
    ) {

        // Month and Year
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlatIconButton(
                icon = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                onClick = {
                    if (initialDate.month == simpleDate.month && initialDate.year == simpleDate.year) return@FlatIconButton
                    simpleDate = if (simpleDate.month == 1) {
                        simpleDate.copy(
                            month = 12,
                            year = simpleDate.year - 1
                        )
                    } else {
                        simpleDate.copy(
                            month = simpleDate.month - 1,
                        )
                    }
                }
            )

            Text(
                modifier = Modifier
                    .weight(1f),
                text = simpleDate.year.toString().formatToHindiIfLanguageIsFa(),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                style = textStyle.copy(
                    fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .weight(1f),
                text = simpleDate.month.asStringMonthName(),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                style = textStyle.copy(
                    fontSize = 24.sp
                )
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = simpleDate.day.padZeroToStartWithPersianDigits(),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                style = textStyle.copy(
                    fontSize = 24.sp
                )
            )
            FlatIconButton(
                icon = Icons.AutoMirrored.Default.KeyboardArrowRight,
                onClick = {
                    simpleDate = if (simpleDate.month == 12) {
                        simpleDate.copy(
                            month = 1,
                            year = simpleDate.year + 1
                        )
                    } else {
                        simpleDate.copy(
                            month = simpleDate.month + 1,
                        )
                    }
                }
            )
        }

        // Weekday headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DatePickerUtils.weekDays().forEach { weekDay ->
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .background(selectedItemBackgroundColor)
                        .padding(vertical = 4.dp),
                    text = weekDay.uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Calendar days
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            itemVerticalAlignment = Alignment.CenterVertically,
            maxItemsInEachRow = 7,
            maxLines = 5
        ) {
            daysList.value.forEach { day ->
                val isDay = day.isNotBlank()
                val isCurrentDay = isDay && day.toInt() == simpleDate.day
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(
                            if (isCurrentDay) {
                                Color.Transparent
                            } else {
                                backGroundColor
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCurrentDay) {
                                selectedItemBackgroundColor
                            } else {
                                Color.Transparent
                            },
                        )
                        .clickable(onClick = {
                            if (isDay) {
                                simpleDate = simpleDate.copy(day = day.toInt())
                            }
                        }),
                ) {
                    dayContent(day, isCurrentDay)
                    Text(
                        modifier = Modifier
                            .align(dayAlign)
                            .padding(4.dp),
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