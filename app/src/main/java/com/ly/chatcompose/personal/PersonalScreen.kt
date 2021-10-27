package com.ly.chatcompose.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ly.chatcompose.R
import com.ly.chatcompose.VoidCallback
import com.ly.chatcompose.ui.theme.ChatComposeTheme
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun PersonalLargeAvatar(scrollState: ScrollState, containerHeight: Dp) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = "large avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .heightIn(max = containerHeight / 2)
            .fillMaxWidth()
            .padding(
                top = offsetDp
            )
    )
}

@Composable
fun PersonalToolbar(
    scrollState: ScrollState,
    containerHeight: Dp,
    onNavIconPressed: VoidCallback = {},
    onMoreEditPress: VoidCallback = {}
) {
    val totalHeight = with(LocalDensity.current) {
        containerHeight.toPx()
    }
    val percent = abs(scrollState.value / totalHeight)
    val tint = if ((1.0f - percent) > 0.05f) Color.White else Color.Black
    val bgColor = Color(255, 255, 255, (255 * percent).roundToInt())
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavIconPressed) {
                Icon(
                    imageVector = Icons.Outlined.NavigateBefore,
                    tint = tint,
                    contentDescription = "back",
                )
            }
        },
        elevation = 0.dp,
        title = {

        },
        backgroundColor = bgColor,
        actions = {
            IconButton(onClick = onMoreEditPress) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "more",
                    tint = tint,
                )
            }
        }
    )
}

@Preview
@Composable
fun PersonalToolbarPre() {
    ChatComposeTheme {
        val scrollState = rememberScrollState()
        PersonalToolbar(scrollState = scrollState, 340.dp)
    }
}

@Preview
@Composable
fun LargeAvatarPre() {
    ChatComposeTheme {
        val scrollState = rememberScrollState()
        PersonalLargeAvatar(scrollState = scrollState, containerHeight = 340.dp)
    }
}
