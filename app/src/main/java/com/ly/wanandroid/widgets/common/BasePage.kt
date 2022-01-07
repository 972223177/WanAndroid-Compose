package com.ly.wanandroid.widgets.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.ComposableCallback
import com.ly.wanandroid.ComposableCallback1
import com.ly.wanandroid.FunctionalityNotAvailablePopup
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.mvi.CommonEvent
import com.ly.wanandroid.mvi.PageStatus
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import com.ly.wanandroid.utils.logD

@Composable
fun BasePage(block: ComposableCallback) {
    val isNightMode by Setting.isNightModeFlow.collectAsState()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, !isNightMode)
    WanAndroidTheme(darkTheme = isNightMode) {
        block()
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Any> BaseScreen(
    pageStatus: State<PageStatus?>,
    commonEvent: State<CommonEvent?>,
    retry: VoidCallback = {},
    emptyHolder: ComposableCallback = {
        EmptyView()
    },
    loadingHolder: ComposableCallback = {
        LoadingView()
    },
    errorHolder: ComposableCallback = {
        ErrorView {
            retry()
        }
    },
    content: ComposableCallback1<T>
) {
    val currPageStatus = remember {
        pageStatus
    }
    val curEvent = remember {
        commonEvent
    }
    val isNightModel by Setting.isNightModeFlow.collectAsState()
    WanAndroidTheme(isNightModel) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (curEvent.value) {
                CommonEvent.DismissLoading -> {}
                CommonEvent.ShowLoading -> {
                    FunctionalityNotAvailablePopup {

                    }
                }
                //toast不能在这里做
                else -> {}
            }
            DisposableEffect(key1 = Unit, effect = {
                logD("init")
                onDispose {
                    logD("dispose")
                }
            })
            when (currPageStatus.value) {
                is PageStatus.Error -> errorHolder()
                PageStatus.Loading -> loadingHolder()
                PageStatus.None -> {}
                is PageStatus.Success -> {
                    val data = (currPageStatus.value as? PageStatus.Success)?.data as? T
                    if (data == null) {
                        errorHolder()
                    } else {
                        content(data)
                    }
                }
                PageStatus.Empty -> emptyHolder()
                else -> {}
            }
        }
    }

}

@Composable
fun ErrorView(retry: VoidCallback) {

}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Empty", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Toast(snackHostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = snackHostState, modifier = modifier) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = SnackbarDefaults.backgroundColor,
            contentColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            Text(text = it.message, modifier = Modifier.padding(8.dp))
        }
    }
}