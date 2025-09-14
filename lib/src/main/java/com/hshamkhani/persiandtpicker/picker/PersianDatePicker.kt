package com.hshamkhani.persiandtpicker.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.components.WheelPicker
import com.hshamkhani.persiandtpicker.utils.DatePickerUtils
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.SimpleDate

/**
 * A composable function that displays a Persian date picker with options
 * for selecting the day, month, and year.
 *
 * @param modifier The modifier to be applied to the time picker.
 * @param textStyle The text style for the time picker options.
 * @param textColor The color of the text for the time picker options.
 * @param selectedTextColor The color of the text for the selected time picker option.
 * @param backGroundColor The background color of the time picker.
 * @param selectedItemBackgroundColor The background color of the selected item in the time picker.
 * @param fontFamily The font family to be used for the text in the time picker.
 * @param onDateSelected A callback function that is invoked when the selected time changes,
 * it receives a `SimpleDate` object representing the selected time.
 * */

@Composable
fun PersianDatePicker(
    modifier: Modifier = Modifier,
    withTimePicker: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    onDateSelected: (date: SimpleDate) -> Unit,
) {

    val isEng = Locale.current.language != "fa"
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(initialDate) }

    LaunchedEffect(simpleDate) {
        onDateSelected(simpleDate)
    }

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

    val days by remember(simpleDate.month) {
        val monthLength =
            DatePickerUtils.initMonthDays(simpleDate.month, simpleDate.year).size
        mutableStateOf(
            DatePickerUtils.initDaysList(monthLength).map { it }
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (withTimePicker) {
            TimePicker(
                modifier = Modifier,
                textStyle = textStyle,
                fontFamily = fontFamily,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
            ) { time ->
                simpleDate = simpleDate.copy(
                    time = time
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        WheelPicker(
            modifier = Modifier,
            options = days,
            initialValueIndex = initialDate.day - 1,
            fontFamily = fontFamily,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            backGroundColor = backGroundColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            onValueSelected = { index, value ->
                simpleDate = simpleDate.copy(
                    day = value.toInt(),
                )
            }
        )
        WheelPicker(
            options = months,
            initialValueIndex = initialDate.month - 1,
            fontFamily = fontFamily,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            backGroundColor = backGroundColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            onValueSelected = { index, _ ->
                simpleDate = simpleDate.copy(
                    month = index + 1 // month is 1-based,
                )
            }
        )
        WheelPicker(
            options = years,
            fontFamily = fontFamily,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            backGroundColor = backGroundColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            onValueSelected = { index, value ->
                simpleDate = simpleDate.copy(
                    year = value.toInt()
                )
            }
        )
    }
}