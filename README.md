# Persian Date Picker
![banner](https://github.com/sham-h93/persian-dtpicker/blob/master/images/banner.png)

[![](https://jitpack.io/v/sham-h93/persian-dtpicker.svg)](https://jitpack.io/#sham-h93/persian-dtpicker) ![Android lib](https://img.shields.io/badge/Android-green) ![JetpackCompose](https://img.shields.io/badge/JetpackCompose-blue)

A simple, minimal, and fully customizable Persian Date Picker for Jetpack Compose.

## Installation

> The library available via [JitPack](https://jitpack.io)
>
**Step 1**  Add JitPack to your **root** `build.gradle.kts` or `settings.gradle`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2**  Add the dependency to the module `build.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.sham-h93:persian-dtpicker:<latest-version>")
}
```
---
### Usage

Minimal usage of `PersianDatePicker` with a time picker:
> Set `withTimePicker = false` to show the date wheels only. `initialDate` is only read on first composition.


```kotlin
@Composable
fun PersianDtPickerExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleDate.now(context = context)) }

    PersianDatePicker(
        modifier = modifier,
        withTimePicker = true,
        initialDate = selectedDate,
        onDateSelected = { date: SimpleDate ->
            selectedDate = date
        },
    )
}
```



Additionally, you can use the `TimePicker` independently:


```kotlin
@Composable
fun TimePickerExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedTime by remember { mutableStateOf(SimpleTime.now(context = context)) }

    TimePicker(
        modifier = modifier,
        initialTime = selectedTime,
        onTimeChanged = { time: SimpleTime ->
            selectedTime = time
        },
    )
}
```

- `PersianDateRailPicker`:

```kotlin
@Composable
fun PersianDateRailPickerExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleDate.now(context = context)) }

   PersianDateRailPicker(
       selectedDate = selectedDate
   ) { date ->
       selectedDate = date
   }
}
```
- `PersianCalendar`:

```kotlin
@Composable
fun PersianCalendarExample(
    modifier: Modifier = Modifier,
    context: Context
) {
    var selectedDate by remember { mutableStateOf(SimpleDate.now(context = context)) }

    PersianCalendar(
        selectedDate = selectedDate,
        onDateSelected = { date ->
            selectedDate = date
        }
    )
}
```


## More Details
|*Class*| *Description* |
|---|---|
|`data class SimpleDate`|Represents a simple date (either gregorian or jalali) with year, month, day, and an optional `SimpleTime` object.|
|`data class SimpleTime`|A time with hour and minute. In 12-hour format `hour` is 1-12 and `clockPeriod` is set; in 24-hour format `hour` is 0-23 and `clockPeriod` is `null`. Pickers follow the device setting.|
|`enum class ClockPeriod`|Clock period (`Am`, `Pm`) for a clock in 12-hour time format.|

---

|*Function*| *Description* |
|---|---|
|`SimpleDate.now(context: Context): SimpleDate`|A function that represents current jalali date and time as a `SimpleDate` object.|
|`SimpleDate.from(timestamp: Long, context: Context): SimpleDate`| A function that converts a timestamp (in milliseconds) to a jalali `SimpleDate` object.|
|`SimpleTime.now(context: Context): SimpleTime`|A function that returns the current time as a `SimpleTime` object.|
|`SimpleTime.of(hourOfDay: Int, minute: Int, is24Hour: Boolean): SimpleTime`|Creates a `SimpleTime` from a 0-23 hour, in 12- or 24-hour form.|
|`fun SimpleTime.hourOfDay(): Int`|The hour as 0-23, whichever format the time is in.|
| `fun SimpleDate.toCalendar(): Calendar` | Converts a Gregorian `SimpleDate` to a Calendar object. |
|`fun SimpleDate.totGregorianDate(): SimpleDate`|Converts a Jalali `SimpleDate` to a Gregorian `SimpleDate`.|

## 👥 Contributing
I sincerely welcome your contributions to the Persian Date Picker project! I would love to have your help in making *PersianDatePicker* better.