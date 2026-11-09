package com.hshamkhani.persiandtpicker.picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.asStringMonthName
import com.hshamkhani.persiandtpicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hshamkhani.persiandtpicker.utils.SimpleDate
import com.hshamkhani.persiandtpicker.utils.dayOfWeek
import com.hshamkhani.persiandtpicker.utils.plusDays
import com.hshamkhani.persiandtpicker.utils.shortWeekDays

/**
 * A horizontally scrolling, endless rail of days.
 *
 * Each item shows the short weekday name on top and the day of month below it. The first day of a
 * month is marked with a month label, but only for months other than the current one; the year is
 * added to the label only when it differs from the current year.
 *
 * @param stickyMonthHeader when `true`, the month labels are replaced by a label pinned over the
 * start of the rail, showing the month (and year, if not the current one) of the first visible
 * day. Days scrolling underneath fade out into [fadeColor].
 * @param fadeColor background behind the sticky label; should match the rail's container color.
 * @param isMarked days for which a small dot is drawn under the day number, e.g. days with events.
 * @param markerColor color of that dot; [selectedMarkerColor] is used on the selected day.
 */
@Composable
fun PersianDateRailPicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    weekDayTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    monthLabelTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedWeekDayColor: Color = MaterialTheme.colorScheme.primary,
    todayTextColor: Color = MaterialTheme.colorScheme.primary,
    monthLabelColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = Color.Transparent,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    stickyMonthHeader: Boolean = false,
    fadeColor: Color = MaterialTheme.colorScheme.surface,
    isMarked: (date: SimpleDate) -> Boolean = { false },
    markerColor: Color = MaterialTheme.colorScheme.primary,
    selectedMarkerColor: Color = selectedTextColor,
    selectedDate: SimpleDate?,
    onDateSelected: (date: SimpleDate) -> Unit,
) {
    val context = LocalContext.current
    val now = remember { SimpleDate.now(context) }
    val today = remember { now.dateOnly() }
    val initialDate = remember { selectedDate ?: now }

    var currentSelectedDate by remember { mutableStateOf(initialDate) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = RAIL_CENTER_INDEX
    )

    Box(modifier = modifier) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
            contentPadding = PaddingValues(horizontal = RAIL_HORIZONTAL_PADDING)
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
                    today = today,
                    isSelected = isSelected,
                    // When sticky, the overlay shows the month instead of a label row.
                    showMonthLabel = !stickyMonthHeader,
                    textStyle = textStyle,
                    weekDayTextStyle = weekDayTextStyle,
                    monthLabelTextStyle = monthLabelTextStyle,
                    textColor = textColor,
                    selectedTextColor = selectedTextColor,
                    selectedWeekDayColor = selectedWeekDayColor,
                    todayTextColor = todayTextColor,
                    monthLabelColor = monthLabelColor,
                    backGroundColor = backGroundColor,
                    selectedItemBackgroundColor = selectedItemBackgroundColor,
                    isMarked = isMarked(date),
                    markerColor = markerColor,
                    selectedMarkerColor = selectedMarkerColor,
                    onClick = {
                        currentSelectedDate = date
                        onDateSelected(date)
                    }
                )
            }
        }

        if (stickyMonthHeader) {
            StickyMonthOverlay(
                modifier = Modifier.matchParentSize(),
                listState = listState,
                initialDate = initialDate,
                today = today,
                textStyle = monthLabelTextStyle,
                color = monthLabelColor,
                fadeColor = fadeColor,
            )
        }
    }
}

@Composable
private fun StickyMonthOverlay(
    modifier: Modifier,
    listState: LazyListState,
    initialDate: SimpleDate,
    today: SimpleDate,
    textStyle: TextStyle,
    color: Color,
    fadeColor: Color,
) {
    val currentMonthDate by remember {
        derivedStateOf {
            dateForIndex(initialDate, listState.firstVisibleItemIndex).let {
                SimpleDate(year = it.year, month = it.month, day = 1)
            }
        }
    }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    AnimatedContent(
        modifier = modifier,
        targetState = currentMonthDate,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "stickyMonth",
    ) { monthDate ->
        // Nothing to show for the current month.
        if (monthDate.year == today.year && monthDate.month == today.month) {
            Spacer(Modifier)
            return@AnimatedContent
        }
        Row(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(fadeColor)
                    .padding(start = RAIL_HORIZONTAL_PADDING, end = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = monthDate.month.asStringMonthName(),
                    style = textStyle,
                    color = color,
                    maxLines = 1,
                )
                if (monthDate.year != today.year) {
                    Text(
                        text = monthDate.year.toString().formatToHindiIfLanguageIsFa(),
                        style = textStyle,
                        color = color,
                        maxLines = 1,
                    )
                }
            }
            // Items fade out underneath the label.
            val fade = listOf(fadeColor, fadeColor.copy(alpha = 0f))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(FADE_WIDTH)
                    .background(Brush.horizontalGradient(if (isRtl) fade.reversed() else fade))
            )
        }
    }
}

@Composable
private fun RailDateItem(
    date: SimpleDate,
    today: SimpleDate,
    isSelected: Boolean,
    showMonthLabel: Boolean,
    textStyle: TextStyle,
    weekDayTextStyle: TextStyle,
    monthLabelTextStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    selectedWeekDayColor: Color,
    todayTextColor: Color,
    monthLabelColor: Color,
    backGroundColor: Color,
    selectedItemBackgroundColor: Color,
    isMarked: Boolean,
    markerColor: Color,
    selectedMarkerColor: Color,
    onClick: () -> Unit,
) {
    val noFontPadding = PlatformTextStyle(includeFontPadding = false)
    val isToday = date.dateOnly() == today

    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    Column(
        modifier = Modifier
            .width(ITEM_WIDTH)
            .then(
                // Without the label row (sticky mode), mark the month start with a divider.
                if (!showMonthLabel && date.day == 1) Modifier.drawBehind {
                    val gap = ITEM_SPACING.toPx() / 2
                    val x = if (layoutDirection == LayoutDirection.Rtl) size.width + gap else -gap
                    drawLine(
                        color = dividerColor,
                        start = Offset(x, size.height * 0.15f),
                        end = Offset(x, size.height * 0.85f),
                        strokeWidth = 1.dp.toPx()
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Month start marker (only for months other than the current one).
        if (showMonthLabel) Box(
            modifier = Modifier
                .height(MONTH_LABEL_HEIGHT)
                .align(Alignment.Start),
            contentAlignment = Alignment.CenterStart
        ) {
            val label = if (date.day == 1) date.monthLabel(today) else null
            if (label != null) {
                Text(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.CenterStart, unbounded = true),
                    text = label,
                    style = monthLabelTextStyle,
                    color = monthLabelColor,
                    maxLines = 1,
                )
            }
        }

        // Week day (fixed height so every item, and the rail, has the same height)
        Box(
            modifier = Modifier.height(WEEK_DAY_HEIGHT),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = shortWeekDays()[date.dayOfWeek() - 1],
                style = weekDayTextStyle.copy(
                    platformStyle = noFontPadding,
                    fontWeight = if (isSelected) FontWeight.Bold else weekDayTextStyle.fontWeight
                ),
                color = if (isSelected) selectedWeekDayColor else textColor,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(8.dp))

        // Day of month
        Box(
            modifier = Modifier
                .size(DAY_BOX_SIZE)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) selectedItemBackgroundColor else backGroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.day.toString().formatToHindiIfLanguageIsFa(),
                style = textStyle.copy(
                    platformStyle = noFontPadding,
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else textStyle.fontWeight
                ),
                color = when {
                    isSelected -> selectedTextColor
                    isToday -> todayTextColor
                    else -> textColor
                },
                textAlign = TextAlign.Center,
            )
            // Sits in the box's bottom padding, under the number, so marked days keep the same size.
            if (isMarked) Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = MARKER_BOTTOM_PADDING)
                    .size(MARKER_SIZE)
                    .clip(CircleShape)
                    .background(if (isSelected) selectedMarkerColor else markerColor)
            )
        }
    }
}

/** Month label for [this] date, or null when it is in the current month and year. */
private fun SimpleDate.monthLabel(today: SimpleDate): String? {
    if (year == today.year && month == today.month) return null
    val monthName = month.asStringMonthName()
    return if (year == today.year) monthName
    else "$monthName ${year.toString().formatToHindiIfLanguageIsFa()}"
}

private fun SimpleDate.dateOnly() = SimpleDate(year = year, month = month, day = day)

private val RAIL_HORIZONTAL_PADDING = 16.dp
private val MONTH_LABEL_HEIGHT = 24.dp
private val FADE_WIDTH = 32.dp
private val ITEM_WIDTH = 48.dp
private val ITEM_SPACING = 4.dp
private val DAY_BOX_SIZE = 40.dp
private val WEEK_DAY_HEIGHT = 20.dp
private val MARKER_SIZE = 4.dp
private val MARKER_BOTTOM_PADDING = 4.dp

private const val RAIL_CENTER_INDEX = 50_000

private fun dateForIndex(
    centerDate: SimpleDate,
    index: Int
): SimpleDate {
    val offset = index - RAIL_CENTER_INDEX
    return centerDate.plusDays(offset)
}
