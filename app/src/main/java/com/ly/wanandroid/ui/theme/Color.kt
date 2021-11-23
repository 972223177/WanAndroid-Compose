package com.ly.wanandroid.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp

val Green27AE60 = Color(0xff27ae60)
val Green3DF48B = Color(0xff3DF48B)
val Green3CDC86 = Color(0xff3cdc86)
val Green67DB9D = Color(0xff67db9d)

val OrangeF86734 = Color(0xfff86734)
val OrangeF78C65 = Color(0xfff78c65)

val Blue4282F4 = Color(0xff4282f4)
val Blue4282F4A60 = Color(0x994282f4)
val Blue3365DB = Color(0xff3365db)
val Blue3365DBA60 = Color(0x993365db)
val Blue73A3F5 = Color(0xff73a3f5)
val Blue73A3F5A05 = Color(0x1173a3f5)
val Blue73A3F5A60 = Color(0x9973a3f5)

val GrayEAEAEA = Color(0xffeaeaea)
val GrayF5F5F5 = Color(0xfff5f5f5)
val GrayF5F5F5A80 = Color(0xccf5f5f5)
val Gray333333 = Color(0xff333333)
val Gray333333A50 = Color(0x88333333)
val Gray666666 = Color(0xff666666)
val Gray999999 = Color(0xff999999)
val GrayCCCCCC = Color(0xffcccccc)

val BlackA93 = Color(0xee000000)
val BlackA50 = Color(0xaa000000)
val BlackA47 = Color(0x77000000)
val BlackA40 = Color(0x66000000)
val BlackA30 = Color(0x4d000000)
val BlackA13 = Color(0x22000000)
val BlackA08 = Color(0x14000000)
val BlackA05 = Color(0x11000000)
val Black111111 = Color(0xff111111)
val Black111111A86 = Color(0xdd111111)
val Black131313 = Color(0xff131313)
val Black181818 = Color(0xff181818)
val Black181818A80 = Color(0xcc181818)
val Black222425 = Color(0xff222425)
val Black222425A93 = Color(0xee222425)

val WhiteA80 = Color(0xccffffff)
val WhiteA70 = Color(0xb3ffffff)
val WhiteA50 = Color(0x88ffffff)
val WhiteA33 = Color(0x54ffffff)
val WhiteA30 = Color(0x4dffffff)
val WhiteA20 = Color(0x33ffffff)
val WhiteA05 = Color(0x11ffffff)

val Colors.textMain: Color
    get() = if (isLight) Blue4282F4 else Blue73A3F5

val Colors.textSecond: Color
    get() = if (isLight) Gray666666 else WhiteA50

val Colors.textThird: Color
    get() = if (isLight) Gray999999 else WhiteA33

val Colors.background1: Color
    get() = if (isLight) GrayF5F5F5 else Black111111

val Colors.textSurface: Color
    get() = if (isLight) Gray333333 else WhiteA70

val Colors.textAccent: Color
    get() = if (isLight) OrangeF86734 else OrangeF78C65



/**
 * 返回完全不透明的颜色，由画布背景色和alpha值合成。适用于不需要半透明背景色的情况
 */
@Composable
fun Colors.compositedOnSurface(alpha: Float): Color =
    onSurface.copy(alpha = alpha).compositeOver(surface)


/**
 * 计算暗黑模式下所对应的颜色
 */
@Composable
fun Colors.elevatedSurface(elevation: Dp): Color = LocalElevationOverlay.current?.apply(
    color = surface,
    elevation = elevation
) ?: surface