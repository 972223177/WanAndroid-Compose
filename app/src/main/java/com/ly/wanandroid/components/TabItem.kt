package com.ly.wanandroid.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.onMainOrSurface
import com.ly.wanandroid.ui.theme.onMainOrSurfaceAlpha

@Composable
fun TabItem(title: String, selected: Boolean, clickable: VoidCallback) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.W500,
        color = if (selected) MaterialTheme.colors.onMainOrSurface else MaterialTheme.colors.onMainOrSurfaceAlpha,
        modifier = Modifier
            .clickable { clickable() }
            .padding(horizontal = 8.dp, vertical = 5.dp)
    )
}