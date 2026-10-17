package com.hshamkhani.persiandtpicker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

/** Number of rows shown above and below the selected row. */
private const val VISIBLE_ROWS_PER_SIDE = 2

@Composable
internal fun WheelPicker(
    modifier: Modifier = Modifier,
    options: List<String>,
    initialValueIndex: Int = 0,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    fontFamily: FontFamily = FontFamily.Default,
    onValueSelected: (index: Int, value: String) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val currentOptions by rememberUpdatedState(options)
    val currentOnValueSelected by rememberUpdatedState(onValueSelected)

    val itemHeight = with(density) { (textStyle.fontSize * 2).toDp() }
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val horizontalPadding = itemHeight / 6

    // Give every row the width of the widest option so the selection band stays put.
    val textMeasurer = rememberTextMeasurer()
    val columnWidth = remember(options, textStyle, fontFamily) {
        val widest = options.maxOfOrNull { option ->
            textMeasurer.measure(
                text = option,
                style = textStyle.copy(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold),
            ).size.width
        } ?: 0
        with(density) { widest.toDp() } + horizontalPadding * 2
    }

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialValueIndex.coerceIn(0, (options.size - 1).coerceAtLeast(0))
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState, SnapPosition.Center)
    val centerIndex by remember(lazyListState) {
        derivedStateOf { lazyListState.centeredItemIndex() }
    }

    // A light tick for every row that crosses the selection band.
    LaunchedEffect(lazyListState) {
        snapshotFlow { centerIndex }
            .distinctUntilChanged()
            .drop(1)
            .collect { haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick) }
    }

    // Report the value once the wheel has settled.
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .filter { isScrolling -> !isScrolling }
            .collect {
                val index = centerIndex.coerceIn(0, currentOptions.lastIndex)
                if (index >= 0) currentOnValueSelected(index, currentOptions[index])
            }
    }

    // When the list shrinks (e.g. switching to a shorter month) bring the selection back in range.
    LaunchedEffect(options.size) {
        if (options.isNotEmpty() && centerIndex > options.lastIndex) {
            lazyListState.animateScrollToItem(options.lastIndex)
        }
    }

    Box(
        modifier = modifier
            .width(columnWidth)
            .height(itemHeight * (VISIBLE_ROWS_PER_SIDE * 2 + 1))
            .background(backGroundColor),
        contentAlignment = Alignment.Center,
    ) {
        // Fixed selection band behind the centered row.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = selectedItemBackgroundColor,
                    shape = RoundedCornerShape(itemHeight / 4),
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .fadingEdges(),
            state = lazyListState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight * VISIBLE_ROWS_PER_SIDE),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(count = options.size, key = { index -> index }) { index ->
                val isSelected = index == centerIndex
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .graphicsLayer {
                            // Distance from the center row in rows, negative when above it.
                            val offset = lazyListState.distanceFromCenter(index, itemHeightPx)
                            val distance = abs(offset).coerceAtMost(VISIBLE_ROWS_PER_SIDE + 1f)
                            val scale = 1f - 0.12f * distance
                            scaleX = scale
                            scaleY = scale
                            alpha = (1f - 0.3f * distance).coerceAtLeast(0.2f)
                            rotationX = (-offset * 22f).coerceIn(-70f, 70f)
                            cameraDistance = 12f * this.density
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { scope.launch { lazyListState.animateScrollToItem(index) } }
                        .wrapContentSize(align = Alignment.Center),
                    text = options[index],
                    style = textStyle,
                    fontFamily = fontFamily,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    color = if (isSelected) selectedTextColor else textColor,
                )
            }
        }
    }
}

/** The index of the item whose center is closest to the viewport center. */
private fun LazyListState.centeredItemIndex(): Int {
    val info = layoutInfo
    val viewportCenter = (info.viewportStartOffset + info.viewportEndOffset) / 2
    return info.visibleItemsInfo
        .minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }
        ?.index ?: firstVisibleItemIndex
}

/** How many rows [index] is away from the viewport center (fractional while scrolling). */
private fun LazyListState.distanceFromCenter(index: Int, itemHeightPx: Float): Float {
    val info = layoutInfo
    val item = info.visibleItemsInfo.firstOrNull { it.index == index } ?: return 0f
    val viewportCenter = (info.viewportStartOffset + info.viewportEndOffset) / 2f
    return (item.offset + item.size / 2f - viewportCenter) / itemHeightPx
}

/** Fades the top and bottom of the content to transparent, whatever is behind the picker. */
private fun Modifier.fadingEdges(): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(
                0f to Color.Transparent,
                0.35f to Color.Black,
                0.65f to Color.Black,
                1f to Color.Transparent,
            ),
            blendMode = BlendMode.DstIn,
        )
    }
