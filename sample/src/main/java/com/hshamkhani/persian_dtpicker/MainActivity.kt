package com.hshamkhani.persian_dtpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hshamkhani.persian_dtpicker.ui.theme.PersiandtpickerTheme
import com.hshamkhani.persiandtpicker.picker.PersianDatePicker
import com.hshamkhani.persiandtpicker.utils.SimpleDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var selectedDate by remember { mutableStateOf(SimpleDate(0, 0, 0)) }

            PersiandtpickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
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
            }
        }
    }
}