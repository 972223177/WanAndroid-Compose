package com.ly.chatcompose.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
    onNavIconPressed: VoidCallback = {},
    onMoreEditPress: VoidCallback = {}
) {


    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Outlined.NavigateBefore,
                contentDescription = "back",
                modifier = Modifier
                    .clickable(onClick = onNavIconPressed)
                    .padding(16.dp)
            )
        },
        title = {

        },
        backgroundColor = Color.White
    )
}

@Preview
@Composable
fun PersonalToolbarPre() {
    ChatComposeTheme {
        val scrollState = rememberScrollState()
        PersonalToolbar(scrollState = scrollState)
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
