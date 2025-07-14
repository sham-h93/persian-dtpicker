package com.hshamkhani.persiandtpicker.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.components.WheelPicker
import com.hshamkhani.persiandtpicker.utils.DateUtils
import java.util.Calendar

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedItemBackgroundClipSize: Dp = 16.dp,
    fontFamily: FontFamily = FontFamily.Default,
    is24h: Boolean = false,
    onTimeChanged: (hour: Int, minutes: Int, amPm: Int?) -> Unit,
) {

    val locale = Locale.current
    val calendar by remember {
        mutableStateOf(
            Calendar.getInstance(locale.platformLocale)
        )
    }

    var selectedTime by remember {
        mutableStateOf(calendar)
    }

    val hoursType = if (is24h) Calendar.HOUR_OF_DAY else Calendar.HOUR
    val isDST = calendar.timeZone.inDaylightTime(calendar.time)
    val calInitHours = calendar.get(hoursType).also { hours ->
        if (isDST) hours - 1 else hours
    }

    val hours by remember(is24h) {
        mutableStateOf(
            DateUtils.initHours(is24h)
        )
    }

    val minutes by remember {
        mutableStateOf(
            DateUtils.initMinutes()
        )
    }

    val amPm by remember {
        mutableStateOf(
            DateUtils.initAmPm(locale.language != "fa")
        )
    }

    LaunchedEffect(selectedTime, is24h) {
        onTimeChanged(
            selectedTime.get(hoursType),
            selectedTime.get(Calendar.MINUTE),
            if (!is24h) selectedTime.get(Calendar.AM_PM) else null
        )
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelPicker(
                modifier = Modifier,
                options = hours,
                initialValue = if (is24h) {
                    calInitHours
                } else {
                    if (calInitHours == 0) {
                        11
                    } else {
                        calInitHours
                    }
                },
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                onValueSelected = { index, _ ->
                    selectedTime = Calendar.getInstance().apply {
                        val hour = if (is24h) index else index + 1
                        set(hoursType, hour)
                        set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
                        if (!is24h) set(Calendar.AM_PM, selectedTime.get(Calendar.AM_PM))
                    }
                }
            )
            Text(
                text = ":",
                style = textStyle,
                color = MaterialTheme.colorScheme.primary
            )
            WheelPicker(
                modifier = Modifier,
                options = minutes,
                initialValue = calendar.get(Calendar.MINUTE),
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                onValueSelected = { insex, _ ->
                    selectedTime = Calendar.getInstance().apply {
                        set(hoursType, selectedTime.get(hoursType))
                        set(Calendar.MINUTE, insex)
                        if (!is24h) set(Calendar.AM_PM, selectedTime.get(Calendar.AM_PM))
                    }
                }
            )
            if (!is24h) {
                WheelPicker(
                    modifier = Modifier,
                    options = amPm.toList(),
                    initialValue = calendar.get(Calendar.AM_PM),
                    fontFamily = fontFamily,
                    textStyle = textStyle,
                    textColor = textColor,
                    selectedTextColor = selectedTextColor,
                    backGroundColor = backGroundColor,
                    selectedItemBackgroundColor = selectedItemBackgroundColor,
                    selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                    onValueSelected = { index, value ->
                        selectedTime = Calendar.getInstance().apply {
                            set(hoursType, selectedTime.get(hoursType))
                            set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
                            set(Calendar.AM_PM, index)
                        }
                    }
                )
            }
        }

    }
}