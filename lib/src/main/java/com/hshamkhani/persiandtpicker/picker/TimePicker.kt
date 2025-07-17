package com.hshamkhani.persiandtpicker.picker

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.components.WheelPicker
import com.hshamkhani.persiandtpicker.utils.ClockPeriod
import com.hshamkhani.persiandtpicker.utils.DateUtils
import com.hshamkhani.persiandtpicker.utils.SimpleTime
import java.time.LocalTime
import java.time.ZoneId


/**
 * A composable function that displays a time picker with options for hours, minutes, and AM/PM (if not in 24-hour format).
 *
 * @param modifier The modifier to be applied to the time picker.
 * @param textStyle The text style for the time picker options.
 * @param textColor The color of the text for the time picker options.
 * @param selectedTextColor The color of the text for the selected time picker option.
 * @param backGroundColor The background color of the time picker.
 * @param selectedItemBackgroundColor The background color of the selected item in the time picker.
 * @param fontFamily The font family to be used for the text in the time picker.
 * @param onTimeChanged A callback function that is invoked when the selected time changes,
 * it receives a `SimpleTime` object representing the selected time.
 *
 **/

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    fontFamily: FontFamily = FontFamily.Default,
    onTimeChanged: (SimpleTime) -> Unit,
) {

    val context = LocalContext.current
    val locale = Locale.current

    val now by remember {
        mutableStateOf(
            LocalTime.now(ZoneId.systemDefault() ?: ZoneId.of("UTC"))
        )
    }

    val is24Hour = DateFormat.is24HourFormat(context)

    var selectedTime by remember {
        mutableStateOf(now)
    }

    var simpleTime by remember(selectedTime) {
        mutableStateOf(
            SimpleTime(
                hour = now.hour,
                minute = now.minute,
                clockPeriod = if (is24Hour) null else {
                    if (now.hour < 12) {
                        ClockPeriod.Am
                    } else {
                        ClockPeriod.Pm
                    }
                }
            )
        )
    }

    val hours by remember {
        mutableStateOf(
            DateUtils.initHours(is24Hour)
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

    LaunchedEffect(simpleTime) {
        onTimeChanged(simpleTime)
    }
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WheelPicker(
            modifier = Modifier,
            options = minutes,
            initialValueIndex = now.minute,
            fontFamily = fontFamily,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            backGroundColor = backGroundColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            onValueSelected = { insex, _ ->
                simpleTime = simpleTime.copy(
                    minute = insex
                )
            }
        )

        Text(
            text = ":",
            style = textStyle,
            color = MaterialTheme.colorScheme.primary
        )

        WheelPicker(
            modifier = Modifier,
            options = hours,
            initialValueIndex = if (is24Hour) now.hour else if (now.hour == 0 || now.hour == 12) 11 else (now.hour % 12) - 1,
            fontFamily = fontFamily,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            backGroundColor = backGroundColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            onValueSelected = { index, value ->
                simpleTime = simpleTime.copy(
                    hour = value.toInt(),
                    clockPeriod = if (is24Hour) null else {
                        if (value.toInt() < 12) ClockPeriod.Am else ClockPeriod.Pm
                    }
                )
            }
        )

        if (!is24Hour) {
            Spacer(Modifier.width(4.dp))
            WheelPicker(
                modifier = Modifier,
                options = amPm.toList(),
                initialValueIndex = if (simpleTime.clockPeriod == ClockPeriod.Am) 0 else 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                onValueSelected = { index, _ ->
                    simpleTime = simpleTime.copy(
                        clockPeriod = if (index == 0) ClockPeriod.Am else ClockPeriod.Pm
                    )
                }
            )
        }
    }
}