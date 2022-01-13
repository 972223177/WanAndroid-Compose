package com.ly.wanandroid.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.ly.wanandroid.VoidCallback

@Composable
fun ConfirmDialog(
    state: ConfirmState,
    content: String,
    negativeText: String,
    positiveText: String,
    title: String = "提示",
    negative: VoidCallback = {},
    positive: VoidCallback
) {
    if (state.showing) {
        AlertDialog(title = {
            if (title.isNotEmpty()) {
                Text(text = title, style = MaterialTheme.typography.h6)
            }
        }, onDismissRequest = {
            negative()
            state.showing = false
        }, text = {
            Text(
                text = content,
                style = MaterialTheme.typography.body2
            )
        }, confirmButton = {
            TextButton(onClick = {
                positive()
                state.showing = false
            }) {
                Text(text = positiveText)
            }
        }, dismissButton = {
            TextButton(onClick = {
                negative()
                state.showing = false
            }) {
                Text(text = negativeText)
            }
        })
    }
}

@Composable
fun rememberConfirmState(initial: Boolean = false): ConfirmState {
    return rememberSaveable(saver = listSaver(
        save = {
            listOf(it.showing)
        },
        restore = {
            ConfirmState(it.firstOrNull() ?: false)
        }
    )) {
        ConfirmState(initial)
    }
}

class ConfirmState(initial: Boolean) {
    var showing by mutableStateOf(initial)
}