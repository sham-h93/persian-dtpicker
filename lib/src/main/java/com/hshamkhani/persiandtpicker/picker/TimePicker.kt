package com.hshamkhani.persiandtpicker.picker

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.components.WheelPicker
import com.hshamkhani.persiandtpicker.utils.SimpleTime
import com.hshamkhani.persiandtpicker.utils.initAmPm
import com.hshamkhani.persiandtpicker.utils.initHours
import com.hshamkhani.persiandtpicker.utils.initMinutes


/**
 * A composable function that displays a time picker with options for hours, minutes, and AM/PM (if not in 24-hour format).
 *
 * @param modifier The modifier to be applied to the time picker.
 * @param initialTime The time shown when the picker first appears.
 * @param textStyle The text style for the time picker options.
 * @param textColor The color of the text for the time picker options.
 * @param selectedTextColor The color of the text for the selected time picker option.
 * @param backGroundColor The background color of the time picker.
 * @param selectedItemBackgroundColor The background color of the selected item in the time picker.
 * @param fontFamily The font family to be used for the text in the time picker.
 * @param onTimeChanged A callback function that is invoked when the selected time changes,
 * it receives a `SimpleTime` object in the device's 12/24-hour format.
 *
 **/

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    initialTime: SimpleTime = SimpleTime.now(LocalContext.current),
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    fontFamily: FontFamily = FontFamily.Default,
    onTimeChanged: (SimpleTime) -> Unit,
) {
    val context = LocalContext.current
    val locale = Locale.current
    val is24Hour = DateFormat.is24HourFormat(context)

    val initialHourOfDay = remember { initialTime.hourOfDay() }
    var hourOfDay by remember { mutableIntStateOf(initialHourOfDay) }
    var minute by remember { mutableIntStateOf(initialTime.minute) }

    val hours = remember(is24Hour) { initHours(is24Hour) }
    val minutes = remember { initMinutes() }
    val amPm = remember(locale) { initAmPm(locale.language != "fa").toList() }

    LaunchedEffect(hourOfDay, minute) {
        onTimeChanged(SimpleTime.of(hourOfDay = hourOfDay, minute = minute, is24Hour = is24Hour))
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelPicker(
                modifier = Modifier,
                options = hours,
                // 24h options are 00-23, 12h options are 01-12.
                initialValueIndex = if (is24Hour) {
                    initialHourOfDay
                } else {
                    (initialHourOfDay % 12).let { if (it == 0) 12 else it } - 1
                },
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                onValueSelected = { index, _ ->
                    hourOfDay = if (is24Hour) {
                        index
                    } else {
                        // Keep the AM/PM half of the day, only change the hour within it.
                        (index + 1) % 12 + if (hourOfDay >= 12) 12 else 0
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
                initialValueIndex = initialTime.minute,
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                onValueSelected = { index, _ -> minute = index }
            )

            if (!is24Hour) {
                Spacer(Modifier.width(4.dp))
                WheelPicker(
                    modifier = Modifier,
                    options = amPm,
                    initialValueIndex = if (initialHourOfDay < 12) 0 else 1,
                    fontFamily = fontFamily,
                    textStyle = textStyle,
                    textColor = textColor,
                    selectedTextColor = selectedTextColor,
                    backGroundColor = backGroundColor,
                    selectedItemBackgroundColor = selectedItemBackgroundColor,
                    onValueSelected = { index, _ ->
                        hourOfDay = hourOfDay % 12 + if (index == 1) 12 else 0
                    }
                )
            }
        }
    }
}
