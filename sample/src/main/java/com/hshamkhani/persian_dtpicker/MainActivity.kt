package com.hshamkhani.persian_dtpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hshamkhani.persian_dtpicker.ui.theme.PersiandtpickerTheme
import com.hshamkhani.persiandtpicker.calendar.PersianCalendar
import com.hshamkhani.persiandtpicker.picker.PersianDatePicker
import com.hshamkhani.persiandtpicker.picker.PersianDateRailPicker
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.toCalendar
import com.hshamkhani.persiandtpicker.utils.totGregorianDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            var selectedDate by remember { mutableStateOf(SimpleDate.now(context)) }
            PersiandtpickerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                with(selectedDate) {
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(year.toString())
                                                append("/")
                                                append(month.toString())
                                                append("/")
                                                append(day.toString())
                                                append(" ")
                                            }
                                            withStyle(
                                                style = SpanStyle(fontSize = 16.sp)
                                            ) {
                                                append(time.hour.toString())
                                                append(":")
                                                append(time.minute.toString())
                                            }
                                            append("\n")
                                            append(
                                                selectedDate.totGregorianDate()
                                                    .toCalendar().time.toString()
                                            )

                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Destinations.Home.name
                    ) {

                        composable(route = Destinations.Home.name) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AppButton(Destinations.WheelDatePicker.name) {
                                    navController.navigate(Destinations.WheelDatePicker.name)
                                }
                                AppButton(Destinations.RailDatePicker.name) {
                                    navController.navigate(Destinations.RailDatePicker.name)
                                }
                                AppButton(Destinations.Calender.name) {
                                    navController.navigate(Destinations.Calender.name)
                                }
                            }
                        }

                        composable(route = Destinations.WheelDatePicker.name) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
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

                        composable(route = Destinations.RailDatePicker.name) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                PersianDateRailPicker(
                                    selectedDate = selectedDate
                                ) { date ->
                                    selectedDate = date
                                }
                            }
                        }

                        composable(route = Destinations.Calender.name) {
                            PersianCalendar(
                                selectedDate = selectedDate,
                                onDateSelected = { date ->
                                    selectedDate = date
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Destinations {
    Home,
    WheelDatePicker,
    RailDatePicker,
    Calender,
}

@Composable
private fun AppButton(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(label)
    }
}
