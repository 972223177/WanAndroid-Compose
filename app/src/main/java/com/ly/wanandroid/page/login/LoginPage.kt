package com.ly.wanandroid.page.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.google.accompanist.insets.statusBarsPadding
import com.ly.wanandroid.R
import com.ly.wanandroid.ui.theme.Blue4282F4
import com.ly.wanandroid.ui.theme.mainOrSurface

enum class LogoAnimState {
    Open, Close
}


@Composable
fun LoginPage() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colors.mainOrSurface)
            .statusBarsPadding()
            .drawBehind {
                val rect = Rect(0f, 100f, size.width, size.height)
                val path = Path().also {
                    it.addArc(rect, 160f, 220f)
                }
                drawIntoCanvas {
                    it.clipPath(path)
                }
            }) {
        AnimLogo(modifier = Modifier.align(Alignment.TopCenter))
    }

}

@Composable
private fun AnimLogo(modifier: Modifier = Modifier) {

    var eyeHeight by remember {
        mutableStateOf(0f)
    }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateValue(
        initialValue = 0f,
        targetValue = eyeHeight,
        typeConverter = TwoWayConverter({
            AnimationVector(it)
        }) {
            it.value
        },
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    Layout(modifier = modifier, content = {
        Logo()
        Eye()
        Eye()
    }, measurePolicy = { measures, constraints ->
        val logoPlaceable = measures[0].measure(constraints)
        val minSize = logoPlaceable.width.coerceAtMost(logoPlaceable.height)
        val eyeSize = (minSize * 0.1f).toInt()
        eyeHeight = eyeSize.toFloat()
        val eyeConstraints = Constraints(
            minWidth = eyeSize, maxHeight = eyeSize, minHeight = eyeSize, maxWidth = eyeSize
        )
        val leftEyePlaceable = measures[1].measure(eyeConstraints)
        val rightEyePlaceable = measures[2].measure(eyeConstraints)
        val leftEyeOffsetX = (minSize * 0.231f).toInt()
        val leftEyeOffsetY = (minSize * 0.45).toInt()
        val rightEyeOffsetX = (minSize * (1 - 0.327f)).toInt()
        val rightEyeOffsetY = (minSize * 0.45).toInt()
        layout(logoPlaceable.width, logoPlaceable.height) {
            logoPlaceable.place(0, 0)
            leftEyePlaceable.place(leftEyeOffsetX, leftEyeOffsetY + offsetY.toInt())
            rightEyePlaceable.place(rightEyeOffsetX, rightEyeOffsetY + offsetY.toInt())
        }
    })
}

@Composable
private fun Logo() {
    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
}

@Composable
private fun Eye() {
    Box(
        modifier = Modifier
            .sizeIn()
            .background(Color.White, CircleShape)
    )
}


@ExperimentalAnimationApi
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun LogoPre() {
    Surface {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .statusBarsPadding()
            .drawBehind {
                val paint = Paint().also {
                    it.color = Blue4282F4
                }
                val bgRect = Rect(0f, 0f, size.width, size.height)
                val ovalRect = Rect(0f, size.height, size.width, size.height)
                val path = Path().also {
                    it.addArc(ovalRect, 160f, 220f)
                }
                drawIntoCanvas {

                    it.clipPath(path)
                    it.drawRect(bgRect, paint)
                }
            })
    }
}