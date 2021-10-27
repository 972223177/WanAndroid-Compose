package com.ly.chatcompose.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
//val Black0c101b = Color()

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