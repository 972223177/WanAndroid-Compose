package com.ly.wanandroid

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FunctionalityNotAvailablePopup(onDismiss: VoidCallback) {
    AlertDialog(onDismissRequest = onDismiss, text = {
        Text(
            text = "Functionality not available \uD83D\uDE48",
            style = MaterialTheme.typography.body2
        )
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(text = "CLOSE")
        }
    })
}