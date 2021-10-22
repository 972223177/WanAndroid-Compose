package com.ly.chatcompose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ly.chatcompose.ComposableCallback1
import com.ly.chatcompose.ValueSetter
import com.ly.chatcompose.ui.theme.ChatComposeTheme

@Composable
fun CScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onProfileClicked: ValueSetter<String>,
    onChatClicked: ValueSetter<String>,
    content: ComposableCallback1<PaddingValues>
) {
    ChatComposeTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                CDrawer(onProfileClicked = onProfileClicked, onChatClicked = onChatClicked)
            },
            content = content
        )
    }
}

@Preview
@Composable
fun CScaffoldPre() {
    CScaffold(onProfileClicked = {}, onChatClicked = {}) {
        Column {
            CAppBar(title = {

            }) {

            }
        }
    }
}