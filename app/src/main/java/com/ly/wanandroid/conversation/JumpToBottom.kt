package com.ly.wanandroid.conversation

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.R
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.ChatComposeTheme

private enum class Visibility {
    VISIBLE, GONE
}

@Composable
fun JumpToBottom(enabled: Boolean, onClicked: VoidCallback, modifier: Modifier = Modifier) {
    val transition = updateTransition(
        targetState = if (enabled) Visibility.VISIBLE else Visibility.GONE,
        label = "jumpToBottom"
    )

    val bottomOffset by transition.animateDp(label = "bottomOffset") {
        if (it == Visibility.VISIBLE) {
            32.dp
        } else {
            (-32).dp
        }
    }
    if (bottomOffset > 0.dp) {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDownward,
                    contentDescription = "arrowDownward",
                    modifier = Modifier.height(18.dp)
                )
            },
            text = { Text(text = stringResource(id = R.string.jumpBottom)) },
            onClick = onClicked,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            modifier = modifier
                .offset(x = 0.dp, y = -bottomOffset)
                .height(36.dp)
        )
    }
}

@Preview
@Composable
fun JumpToBottomPre() {
    ChatComposeTheme {
        Surface {
            JumpToBottom(enabled = true, onClicked = { })
        }
    }
}