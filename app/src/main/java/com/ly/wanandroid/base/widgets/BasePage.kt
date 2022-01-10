package com.ly.wanandroid.base.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.*
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.base.mvi.CommonEvent
import com.ly.wanandroid.base.mvi.IViewAction
import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.PageStatus
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import com.ly.wanandroid.base.utils.logD

@Composable
fun BasePage(
    navController: NavHostController,
    darkIcons:Boolean = false,
    block: ComposableCallback
) {
    val isNightMode by Setting.isNightModeFlow.collectAsState()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = darkIcons)
    WanAndroidTheme(darkTheme = isNightMode) {
        CompositionLocalProvider(LocalNavController provides navController) {
            block()
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Any> BaseScreen(
    viewModel: MviViewModel<out IViewAction>,
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
    val currPageStatus = viewModel.pageState.observeAsState()
    val curEvent = viewModel.commonEvent.observeAsState()
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
            viewModel.initial()
            onDispose {
                logD("${viewModel::class.java.simpleName}:dispose")
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