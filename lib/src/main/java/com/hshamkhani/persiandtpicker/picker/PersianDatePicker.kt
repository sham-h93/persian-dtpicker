package com.hshamkhani.persiandtpicker.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.toArabicDigits
import ir.huri.jcal.JalaliCalendar

@Composable
fun PersianDatePicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedItemBackgroundClipSize: Dp = 16.dp,
    isEnglish: Boolean? = null,
    onDateSelected: (jYear: Int, jMonth: Int, jDay: Int) -> Unit,
) {

    val isEng = isEnglish ?: (Locale.current.language != "fa")

    val initialDate by remember {
        mutableStateOf(JalaliCalendar())
    }

    var selectedDate by remember {
        mutableStateOf(initialDate)
    }

    val years by remember {
        mutableStateOf(
            DateUtils.initYearList(initialDate.year).map { it.toString().formatToHindiIfLanguageIsFa() })
    }

    val months by remember {
        mutableStateOf(
            DateUtils.initMonthList(isEng = isEng)
        )
    }

    val days by remember(selectedDate.month) {
        if (selectedDate.monthLength == 30 && selectedDate.day == 31) {
            selectedDate.day -= 1
        }
        mutableStateOf(
            DateUtils.initDaysList(selectedDate.monthLength).map { it }
        )
    }

    LaunchedEffect(selectedDate) {
        onDateSelected(
            selectedDate.year,
            selectedDate.month,
            selectedDate.day,
        )
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            WheelPicker(
                modifier = Modifier,
                options = days,
                initialValue = initialDate.day - 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = index + 1
                        month = selectedDate.month
                        year = selectedDate.year
                    }
                }
            )
            WheelPicker(
                options = months,
                initialValue = initialDate.month - 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = selectedDate.day
                        month = index + 1
                        year = selectedDate.year
                    }
                }
            )
            WheelPicker(
                options = years,
                initialValue = 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                selectedItemBackgroundClipSize = selectedItemBackgroundClipSize,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = selectedDate.day
                        month = selectedDate.month
                        year = years[index].toArabicDigits()
                    }
                }
            )
        }
    }
}