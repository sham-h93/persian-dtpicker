package com.hshamkhani.persian_dtpicker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hshamkhani.persian_dtpicker.ui.theme.PersiandtpickerTheme
import com.hshamkhani.persiandtpicker.calendar.PersianCalendar
import com.hshamkhani.persiandtpicker.picker.PersianDatePicker
import com.hshamkhani.persiandtpicker.picker.TimePicker
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.SimpleTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersiandtpickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SamplePersianCalendar(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SamplePersianCalendar(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(SimpleDate.now(context = context)) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PersianCalendar(
            modifier = Modifier.fillMaxWidth(),
            dayAlign = Alignment.TopStart,
            textStyle = MaterialTheme.typography.titleMedium,
            textColor = MaterialTheme.colorScheme.onBackground,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            backGroundColor = MaterialTheme.colorScheme.background,
            selectedItemBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
            dayContent = { day, isSelected ->
                if (isSelected) Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "+"
                )
            },
            selectedDate = selectedDate,
            onDateSelected = { simpleDate ->
                selectedDate = simpleDate
            }
        )
    }
}

@Composable
fun SamplePersianDatePicker(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleDate(0, 0, 0)) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            text = "$selectedDate"
        )

        PersianDatePicker(
            withTimePicker = true,
            onDateSelected = { date ->
                selectedDate = date
            },
            textStyle = MaterialTheme.typography.titleLarge,
            textColor = MaterialTheme.colorScheme.onBackground,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            backGroundColor = MaterialTheme.colorScheme.background,
            selectedItemBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
    }
}

@Composable
fun PersianDtPickerExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleDate.now(context = context)) }

    PersianDatePicker(
        modifier = modifier,
        withTimePicker = true,
        onDateSelected = { date: SimpleDate ->
            selectedDate = date
        },
    )
}

@Composable
fun TimePickerExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleTime.now(context = context)) }

    TimePicker(
        modifier = modifier,
        onTimeChanged = { time: SimpleTime ->
            selectedDate = time
        },
    )
}