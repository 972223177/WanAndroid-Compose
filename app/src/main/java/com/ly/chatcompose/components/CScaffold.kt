package com.ly.chatcompose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ly.chatcompose.ui.theme.ChatComposeTheme

@Composable
fun CScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
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