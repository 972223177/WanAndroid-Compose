package com.ly.wanandroid.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.ComposableCallback
import com.ly.wanandroid.ValueGetter
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import kotlin.math.roundToInt

@Composable
fun AnimatingFabContent(
    icon: ComposableCallback,
    text: ComposableCallback,
    modifier: Modifier = Modifier,
    extended: Boolean = true
) {
    val currentState = if (extended) ExpandableFabStates.Extended else ExpandableFabStates.Collapsed
    val transition = updateTransition(targetState = currentState, "")
    val textOpacity by transition.animateFloat(transitionSpec = {
        if (targetState == ExpandableFabStates.Collapsed) {
            tween(
                easing = LinearEasing,
                durationMillis = (transitionDuration / 12f * 5).roundToInt()
            )
        } else {
            tween(
                easing = LinearEasing, delayMillis = (transitionDuration / 3f).roundToInt(),
                durationMillis = (transitionDuration / 12f * 5).roundToInt()
            )
        }
    }, label = "") { progress ->
        if (progress == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }
    val fabWidthFactor by transition.animateFloat(transitionSpec = {
        if (targetState == ExpandableFabStates.Collapsed) {
            tween(easing = FastOutSlowInEasing, durationMillis = transitionDuration)
        } else {
            tween(easing = FastOutSlowInEasing, durationMillis = transitionDuration)
        }
    }, label = "") { progress ->
        if (progress == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }
    IconAndTextRow(
        icon = icon,
        text = text,
        opacityProgress = { textOpacity },
        widthProgress = { fabWidthFactor },
        modifier = modifier
    )

}

@Composable
private fun IconAndTextRow(
    icon: ComposableCallback,
    text: ComposableCallback,
    opacityProgress: ValueGetter<Float>,
    widthProgress: ValueGetter<Float>,
    modifier: Modifier
) {
    Layout(content = {
        icon()
        Box(modifier = Modifier.alpha(opacityProgress())) {
            text()
        }
    }, modifier = modifier) { measurables, constraints ->
        val iconPlaceable = measurables[0].measure(constraints)
        val textPlaceable = measurables[1].measure(constraints)
        val height = constraints.maxHeight
        val initialWidth = height.toFloat()
        val iconPadding = (initialWidth - iconPlaceable.width) / 2f
        val expandedWidth = iconPlaceable.width + textPlaceable.width + iconPadding * 3
        val width = androidx.compose.ui.util.lerp(initialWidth, expandedWidth, widthProgress())
        layout(width = width.roundToInt(), height = height) {
            iconPlaceable.place(
                iconPadding.roundToInt(),
                constraints.maxHeight / 2 - iconPlaceable.height / 2
            )
            textPlaceable.place(
                (iconPlaceable.width + iconPadding * 2).roundToInt(),
                constraints.maxHeight / 2 - textPlaceable.height / 2
            )
        }
    }
}

private enum class ExpandableFabStates {
    Collapsed, Extended
}

private const val transitionDuration = 200


@Preview
@Composable
fun AnimFabContentPre() {
    WanAndroidTheme {
        Surface {
            Scaffold {
                Box(Modifier.fillMaxSize()) {
                    var collapsed by remember {
                        mutableStateOf(false)
                    }
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Cyan)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .clickable {
                                collapsed = !collapsed
                            }
                    )
                    AnimatingFabContent(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Message,
                                contentDescription = ""
                            )
                        },
                        text = {
                            Text(text = "Message", color = Color.White)
                        },
                        extended = !collapsed,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp)
                            .height(45.dp)
                            .background(Color.Cyan)
                    )
                }
            }
        }
    }
}