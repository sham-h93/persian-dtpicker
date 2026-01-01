package com.hshamkhani.persiandtpicker.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.R
import com.hshamkhani.persiandtpicker.components.FlatIconButton
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.asStringMonthName
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.padZeroToStartWithPersianDigits
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.SimpleTime
import com.hshamkhani.persiandtpicker.utils.dayOfWeek
import com.hshamkhani.persiandtpicker.utils.initMonthDays
import com.hshamkhani.persiandtpicker.utils.weekDays

@Composable
fun PersianCalendar(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    PersianCalendarImpl(
        modifier = modifier,
        textStyle = textStyle,
        textColor = textColor,
        backGroundColor = backGroundColor,
        selectedItemBackgroundColor = selectedItemBackgroundColor,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
    )
}

@Composable
fun PersianCalendar(
    modifier: Modifier = Modifier,
    dayAlign: Alignment = Alignment.Center,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBorderColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    dayContent: @Composable BoxScope.(String, Boolean) -> Unit,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    PersianCalendarImpl(
        modifier = modifier,
        dayAlign = dayAlign,
        textStyle = textStyle,
        textColor = textColor,
        backGroundColor = backGroundColor,
        selectedItemBackgroundColor = selectedItemBorderColor,
        dayContent = dayContent,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
    )
}

@Composable
private fun PersianCalendarImpl(
    modifier: Modifier = Modifier,
    dayAlign: Alignment = Alignment.Center,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    dayContent: (@Composable BoxScope.(String, Boolean) -> Unit)? = null,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(selectedDate ?: initialDate) }

    val daysInMonth by remember(simpleDate.month) {
        mutableStateOf(
            initMonthDays(
                monthNumber = simpleDate.month,
                year = simpleDate.year
            )
        )
    }

    val daysList = remember(daysInMonth) {
        derivedStateOf {
            simpleDate = simpleDate.copy(time = SimpleTime())
            val start = simpleDate.copy(day = 1).dayOfWeek() - 1
            val end = 7 - simpleDate.copy(day = daysInMonth.last()).dayOfWeek() + 1

            val monthDays = (1..daysInMonth.last()).map {
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
                icon = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_left),
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
                style = textStyle
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .weight(2f),
                text = simpleDate.month.asStringMonthName(),
                textAlign = TextAlign.Center,
                style = textStyle
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = simpleDate.day.padZeroToStartWithPersianDigits(),
                textAlign = TextAlign.Center,
                style = textStyle
            )
            FlatIconButton(
                icon = ImageVector.vectorResource(id = R.drawable.keyboard_arrow_right),
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
            weekDays().forEach { weekDay ->
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .background(selectedItemBackgroundColor)
                        .padding(vertical = 4.dp),
                    text = weekDay.uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    style = textStyle
                )
            }
        }

        // Calendar days
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            itemVerticalAlignment = Alignment.CenterVertically,
            maxItemsInEachRow = 7,
            maxLines = 6
        ) {
            daysList.value.forEach { day ->
                val isDay = day.isNotBlank()
                val isCurrentDay = isDay && day.toInt() == simpleDate.day

                // Day Filler
                if (!isDay) {
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }

                // Day with content
                if (dayContent != null && isDay) {
                    DayWitchContent(
                        isCurrentDay = isCurrentDay,
                        selectedBorderColor = selectedItemBackgroundColor,
                        onClick = { simpleDate = simpleDate.copy(day = day.toInt()) },
                        content = {
                            dayContent(day, isCurrentDay)
                            Text(
                                modifier = Modifier
                                    .align(dayAlign)
                                    .padding(4.dp),
                                text = day,
                                style = textStyle,
                                color = textColor,
                            )
                        }
                    )
                }

                // Simple day
                if (dayContent == null && isDay) {
                    Day(
                        isCurrentDay = isCurrentDay,
                        backGroundColor = backGroundColor,
                        selectedDaybackgroundColor = selectedItemBackgroundColor,
                        onClick = { simpleDate = simpleDate.copy(day = day.toInt()) },
                        content = {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                text = day,
                                style = textStyle,
                                color = textColor,
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FlowRowScope.DayWitchContent(
    modifier: Modifier = Modifier,
    isCurrentDay: Boolean,
    selectedBorderColor: Color,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f)
            .background(selectedBorderColor.copy(alpha = if (isCurrentDay) .1f else 0f))
            .border(
                width = 1.dp,
                color = if (isCurrentDay) {
                    selectedBorderColor
                } else {
                    Color.Transparent
                },
            )
            .clickable(onClick = onClick),
        content = content
    )
}

@Composable
private fun FlowRowScope.Day(
    modifier: Modifier = Modifier,
    isCurrentDay: Boolean,
    backGroundColor: Color,
    selectedDaybackgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.large)
            .background(
                if (isCurrentDay) {
                    selectedDaybackgroundColor
                } else {
                    backGroundColor
                }
            )
            .clickable(onClick = onClick),
        content = content
    )
}