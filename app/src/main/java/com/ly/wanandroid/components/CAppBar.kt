package com.ly.wanandroid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.ComposableExtCallback
import com.ly.wanandroid.R
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.ChatComposeTheme
import com.ly.wanandroid.ui.theme.elevatedSurface

@Composable
fun CAppBar(
    modifier: Modifier = Modifier,
    onNavIconPress: VoidCallback = {},
    title: ComposableExtCallback<RowScope>,
    actions: ComposableExtCallback<RowScope> = {},
) {
    val backgroundColor = MaterialTheme.colors.elevatedSurface(elevation = 3.dp)
    Column(Modifier.background(backgroundColor.copy(alpha = 0.95f))) {
        TopAppBar(
            title = {
                Row {
                    title()
                }
            },
            modifier = modifier,
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = MaterialTheme.colors.onSurface,
            actions = actions,
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_jetchat),
                    contentDescription = stringResource(
                        id = R.string.nav_app_bar_navigate_up_description
                    ),
                    modifier = Modifier
                        .clickable(onClick = onNavIconPress)
                        .padding(horizontal = 16.dp)
                )
            }
        )
        Divider()
    }
}


@Preview
@Composable
fun AppBarPreview() {
    CAppBar(title = {
        Text(text = "Preview")
    })

}

@Preview
@Composable
fun AppBarPreviewDark() {
    ChatComposeTheme(true) {
        CAppBar(title = {
            Text(text = "Preview")
        })
    }
}