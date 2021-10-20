package com.ly.chatcompose.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navIconPress: () -> Unit = {},
    title: @Composable RowScope.() -> Unit,
    action: @Composable RowScope.() -> Unit = {}
) {
    val backgroundColor = MaterialTheme.colors
}


@Preview
@Composable
fun AppBarPreview() {
    AppBar(title = {
        Text(text = "Preview")
    })
}