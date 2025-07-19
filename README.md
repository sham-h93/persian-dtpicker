# Persian Date Picker
![banner](https://github.com/sham-h93/persian-dtpicker/blob/master/images/banner.png)

[![](https://jitpack.io/v/sham-h93/persian-dtpicker.svg)](https://jitpack.io/#sham-h93/persian-dtpicker) ![Android lib](https://img.shields.io/badge/Android-green) ![JetpackCompose](https://img.shields.io/badge/JetpackCompose-blue)

A simple, minimal and fully customizable Persian Date Picker for Jetpack Compose.

## Installation

> The library available via [JitPack](https://jitpack.io)
>
**Step 1**  Add JitPack to your **root** `build.gradle.kts` or `settings.gradle`:


	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			maven { url = uri("https://jitpack.io") }
		}
	}

**Step 2**  Add the dependency to the module `build.gradle.kts`

```css
dependencies {
		implementation("com.github.sham-h93:persian-dtpicker:<latest-version>")
}
```

### Usage
---

Minimal usage of `PersianDatePicker` with a time picker:
>You can also use it without time picker by setting property  `withTimePicker` to ` false`.


```css
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
```



Additionally, you can use the `TimePicker` independently:


```css
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
```
---
|*Class*| *Description* |
|---|---|
|`data class SimpleDate`|Represents a simple date (either gregorian or jalali) with year, month, day, and an optional `SimpleTime` object.|
|`data class SimpleTime`|Represents a time with hour, minute, and an `ClockPeriod` (`Am`/`Pm`), if the clock in 12-hour time format.|
|`enum class ClockPeriod`|Clock period (`Am`, `Pm`) for a clock in 12-hour time format.|

---


|*Function*| *Description* |
|---|---|
|`SimpleDate.now(context: Context): SimpleDate`|A function that represents current jalali date and time as a `SimpleDate` object.|
|`SimpleDate.from(timestamp: Long, context: Context): SimpleDate`| A function that converts a timestamp (in milliseconds) to a jalali `SimpleDate` object.|
|`SimpleTime.now(context: Context): SimpleTime`|A function that returns the current time as a `SimpleTime` object.|
|||	
| `fun SimpleDate.toCalendar(): Calendar` | Converts a Gregorian `SimpleDate` to a Calendar object. |
|`fun SimpleDate.totGregorianDate(): SimpleDate`|Converts a Jalali `SimpleDate` to a Gregorian `SimpleDate`.|