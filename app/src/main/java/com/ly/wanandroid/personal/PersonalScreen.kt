package com.ly.wanandroid.personal

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.R
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.ChatComposeTheme
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun PersonalHeader(scrollState: ScrollState, containerHeight: Dp) {

}

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
    modifier: Modifier = Modifier,
    onNavIconPressed: VoidCallback = {},
    onMoreEditPress: VoidCallback = {}
) {
    var bg by remember {
        mutableStateOf(Color.Transparent)
    }
    var tint by remember {
        mutableStateOf(Color.White)
    }
    val totalHeight = with(LocalDensity.current) {
        containerHeight.toPx()
    }

    LaunchedEffect(key1 = scrollState.value, block = {
        val percent = abs(scrollState.value / totalHeight)
        val value = ((1f - percent) * 255).roundToInt()
        tint = Color(255, value, value, 255)
        bg = Color(255, 255, 255, (255 * percent).roundToInt())

    })
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
        modifier = modifier,
        backgroundColor = bg,
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
        Box(modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {

                        PersonalLargeAvatar(
                            scrollState = scrollState,
                            containerHeight = this@BoxWithConstraints.maxHeight
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(840.dp)
                                .background(Color.Cyan)
                        )
                    }
                }
            }
            PersonalToolbar(
                scrollState = scrollState,
                340.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
        }

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
