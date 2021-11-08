package com.ly.wanandroid.conversation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.*
import com.ly.wanandroid.VoidCallback

/**
 * 拦截返回操作
 */
@Composable
fun BackPressHandler(onBackPressed: VoidCallback) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    val backDispatcher = LocalBackPressedDispatcher.current

    DisposableEffect(key1 = backDispatcher, effect = {
        backDispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    })
}


val LocalBackPressedDispatcher =
    staticCompositionLocalOf<OnBackPressedDispatcher> { error("No Back Dispatcher provided") }