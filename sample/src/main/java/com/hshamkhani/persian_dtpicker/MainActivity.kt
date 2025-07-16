package com.hshamkhani.persian_dtpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hshamkhani.persian_dtpicker.ui.theme.PersiandtpickerTheme
import com.hshamkhani.persiandtpicker.picker.PersianDatePicker
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.totGregorianDate
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var selectedDate by remember { mutableStateOf(SimpleDate(0, 0, 0)) }
            LaunchedEffect(selectedDate) {
                val ts = selectedDate.totGregorianDate().toTimestampSeconds()
                println("Selected Date Timestamp: ${Date(ts * 1000)}")
            }

            PersiandtpickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(16.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
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