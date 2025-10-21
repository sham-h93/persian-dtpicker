package com.hshamkhani.persiandtpicker.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.asStringMonthName
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.initMonthDays
import com.hshamkhani.persiandtpicker.utils.initYearList

@Composable
fun RailPicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    RailPickerImpl(
        modifier = modifier,
        textStyle = textStyle,
        fontFamily = fontFamily,
        textColor = textColor,
        selectedTextColor = selectedTextColor,
        backGroundColor = backGroundColor,
        selectedItemBackgroundColor = selectedItemBackgroundColor,
        selectedDate = selectedDate,
        onDateSelected = onDateSelected
    )
}

@Composable
private fun RailPickerImpl(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedDate: SimpleDate? = null,
    onDateSelected: (date: SimpleDate) -> Unit
) {
    val context = LocalContext.current

    val initialDate by remember { mutableStateOf(SimpleDate.now(context = context)) }
    var simpleDate by remember { mutableStateOf(selectedDate ?: initialDate) }

    val years by remember {
        mutableStateOf(
            initYearList(initialDate.year).map { year ->
                (1..12).map { month ->
                    initMonthDays(
                        monthNumber = month,
                        year = year
                    ).map { it.asStringMonthName() }.toList()
                }.flatten()
            }
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        years.forEach {
            items(
                count = it.size
            ) { year ->

            }
        }
    }
}

@Composable
fun DayCard(
    modifier: Modifier = Modifier,
    month: String,
    day: String,
) {
    Card {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = month)
            Text(text = day)
        }
    }
}

