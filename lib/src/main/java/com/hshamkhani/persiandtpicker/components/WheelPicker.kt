package com.hshamkhani.persiandtpicker.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun WheelPicker(
    modifier: Modifier = Modifier,
    options: List<String>,
    initialValue: Int = 0,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    selectedItemBackgroundClipSize: Dp = 16.dp,
    fontFamily: FontFamily = FontFamily.Default,
    onValueSelected: (index: Int, value: String) -> Unit,
) {


    var selectedIndex by remember { mutableIntStateOf(initialValue) }
    val denisty = LocalDensity.current
    val itemSize = with(denisty) { (textStyle.fontSize * 2).toDp() }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialValue
    )

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val index = lazyListState.firstVisibleItemIndex
                    val middleIndex = index
                    lazyListState.animateScrollToItem(middleIndex)
                    selectedIndex = middleIndex
                    onValueSelected(selectedIndex, options[index])
                }
            }
    }

    Box(
        modifier = modifier.height((itemSize * 3)),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .animateContentSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.Center,
        ) {
            item { FillerItem(modifier = Modifier.height(itemSize)) }
            items(count = options.size, key = { index -> index }) { item ->
                Text(
                    modifier = Modifier
                        .height(itemSize)
                        .wrapContentSize(align = Alignment.Center)
                        .clip(RoundedCornerShape(selectedItemBackgroundClipSize))
                        .background(
                            if (item == selectedIndex) {
                                selectedItemBackgroundColor
                            } else {
                                backGroundColor
                            }
                        )
                        .defaultMinSize(minWidth = itemSize)
                        .clickable { selectedIndex = item }
                        .padding(8.dp),
                    text = options[item],
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily,
                    color = if (item == selectedIndex) {
                        selectedTextColor
                    } else {
                        textColor
                    }
                )
            }
            item { FillerItem(modifier = Modifier.height(itemSize)) }
        }
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .height(itemSize)
                .align(Alignment.TopStart)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    .1f to backGroundColor,
                    .3f to Color.Unspecified,
                    .7f to Color.Unspecified,
                    .9f to backGroundColor,
                )
            )
        }
    }

}

@Composable
private fun FillerItem(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {}
}