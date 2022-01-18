package com.ly.wanandroid.page.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.R
import com.ly.wanandroid.base.widgets.WAppBar
import com.ly.wanandroid.base.widgets.WAppBarHeight
import com.ly.wanandroid.ui.theme.mainOrSurface
import com.ly.wanandroid.ui.theme.onMainOrSurface

private object ConstraintIds {
    const val Header = "header"
    const val Logo = "AnimLogo"
    const val Title = "WelcomeText"
    const val CloseBtn = "CloseBtn"
    const val Bg = "MainBg"
    const val Vp = "viewPager"
}

private enum class EyeState {
    Open, Close
}

private val headerConstraints by lazy(LazyThreadSafetyMode.NONE) {
    ConstraintSet {
        val bg = createRefFor(ConstraintIds.Bg)
        val logo = createRefFor(ConstraintIds.Logo)
        val closeBtn = createRefFor(ConstraintIds.CloseBtn)
        val title = createRefFor(ConstraintIds.Title)
        val vp = createRefFor(ConstraintIds.Vp)
        constrain(closeBtn) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(bg) {
            top.linkTo(closeBtn.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(vp) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(bg.bottom, margin = (-58).dp)

        }
        constrain(logo) {
            top.linkTo(closeBtn.bottom)
            centerHorizontallyTo(parent)
        }
        constrain(title) {
            top.linkTo(logo.bottom)
            centerHorizontallyTo(parent)
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoginPage() {
    Scaffold {
        ConstraintLayout(
            constraintSet = headerConstraints,
            modifier = Modifier.layoutId(ConstraintIds.Header)
        ) {
//            HorizontalPager(
//                count = 2,
//                modifier = Modifier
//                    .layoutId(ConstraintIds.Vp)
//                    .fillMaxWidth()
//                    .height(300.dp)
//                    .background(Color.Red)
//            ) {
//
//            }
            HeaderBg(modifier = Modifier.layoutId(ConstraintIds.Bg))
            CloseBtn(modifier = Modifier.layoutId(ConstraintIds.CloseBtn))
            AnimLogo(modifier = Modifier.layoutId(ConstraintIds.Logo))
            Title(modifier = Modifier.layoutId(ConstraintIds.Title))

        }
    }

}

@Composable
private fun CloseBtn(modifier: Modifier = Modifier) {
    WAppBar(modifier = modifier, navigationIcon = {
        val navController = LocalNavController.current
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.Sharp.Close,
                contentDescription = "close",
                tint = MaterialTheme.colors.onMainOrSurface
            )
        }
    })
}

@Composable
private fun HeaderBg(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(260.dp - WAppBarHeight)
            .background(MaterialTheme.colors.mainOrSurface, shape = object : Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density
                ): Outline {
                    val path = Path().also {
                        it.moveTo(0f, 0f)
                        it.lineTo(0f, size.height)
                        it.quadraticBezierTo(
                            size.width / 2f,
                            size.height - 300f,
                            size.width,
                            size.height
                        )
                        it.lineTo(size.width, 0f)
                        it.lineTo(0f, 0f)
                    }
                    return Outline.Generic(path)
                }

            })
    )
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        text = "欢迎使用",
        fontSize = 22.sp,
        color = MaterialTheme.colors.onMainOrSurface,
        modifier = modifier
    )
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
            animation = tween(200, easing = FastOutSlowInEasing, delayMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )


    Layout(modifier = modifier.size(100.dp), content = {
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
    LoginPage()
}