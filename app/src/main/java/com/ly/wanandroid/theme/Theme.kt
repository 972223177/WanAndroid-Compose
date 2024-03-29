package com.ly.wanandroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.ly.wanandroid.theme.Blue4282F4
import com.ly.wanandroid.theme.Blue73A3F5

@Composable
fun WanAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) darkColors(
            primary = Blue73A3F5,
        ) else lightColors(
            primary = Blue4282F4
        ),
        content = content
    )
}