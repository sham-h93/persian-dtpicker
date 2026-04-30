package com.hshamkhani.persiandtpicker.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.padZeroToStartWithPersianDigits
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.plusDays

@Composable
fun PersianDateRailPicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    backGroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedDate: SimpleDate?,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    PersianDateRailPickerImpl(
        modifier = modifier,
        textStyle = textStyle,
        textColor = textColor,
        selectedTextColor = selectedTextColor,
        backGroundColor = backGroundColor,
        selectedItemBackgroundColor = selectedItemBackgroundColor,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected
    )
}

@Composable
private fun PersianDateRailPickerImpl(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedDate: SimpleDate?,
    onDateSelected: (SimpleDate) -> Unit
) {
    val context = LocalContext.current
    val initialDate = remember {
        selectedDate ?: SimpleDate.now(context)
    }

    var currentSelectedDate by remember {
        mutableStateOf(initialDate)
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = RAIL_CENTER_INDEX
    )

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { it }
        ) { index ->

            val date = dateForIndex(initialDate, index)
            val isSelected by remember {
                derivedStateOf { date == currentSelectedDate }
            }

            RailDateItem(
                date = date,
                isSelected = isSelected,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                backGroundColor = backGroundColor,
                selectedItemBackgroundColor = selectedItemBackgroundColor,
                onClick = {
                    currentSelectedDate = date
                    onDateSelected(date)
                }
            )
        }
    }
}

@Composable
private fun RailDateItem(
    date: SimpleDate,
    isSelected: Boolean,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    backGroundColor: Color,
    selectedItemBackgroundColor: Color,
    onClick: () -> Unit,
) {
    val topText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                color = if (isSelected) selectedTextColor else textColor
            ),
            block = {
                append(date.month.padZeroToStartWithPersianDigits().formatToHindiIfLanguageIsFa())
                append("/")
            }
        )
        withStyle(
            style = SpanStyle(
                fontSize = 18.sp,
                color = if (isSelected) selectedTextColor else textColor
            ),
            block = {
                append(date.day.padZeroToStartWithPersianDigits().formatToHindiIfLanguageIsFa())
            }
        )
    }

    Column(
        modifier = Modifier
            .widthIn(min = 56.dp)
            .clip(MaterialTheme.shapes.large)
            .background(
                if (isSelected)
                    selectedItemBackgroundColor
                else
                    backGroundColor
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Day
        Text(
            modifier = Modifier.wrapContentSize(unbounded = true),
            text = topText,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            ),
            color = if (isSelected) selectedTextColor else textColor
        )

        // Year
        Text(
            text = date.year.toString().formatToHindiIfLanguageIsFa(),
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                fontSize = (textStyle.fontSize.value / 3 * 2).sp
            ),
            color = if (isSelected) selectedTextColor else textColor
        )
    }
}


private const val RAIL_CENTER_INDEX = 50_000

private fun dateForIndex(
    centerDate: SimpleDate,
    index: Int
): SimpleDate {
    val offset = index - RAIL_CENTER_INDEX
    return centerDate.plusDays(offset)
}