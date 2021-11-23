package com.ly.wanandroid.widgets.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.ComposableCallback
import com.ly.wanandroid.ComposableCallback1
import com.ly.wanandroid.FunctionalityNotAvailablePopup
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.mvi.CommonEvent
import com.ly.wanandroid.mvi.PageStatus
import com.ly.wanandroid.utils.logD
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun <T : Any> BaseScreen(
    pageStatus: State<PageStatus<T>?>,
    commonEvent: State<CommonEvent?>,
    msgEvent: StateFlow<String>,
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
    Box(modifier = Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val snackHostState = remember {
            SnackbarHostState()
        }
        when (commonEvent.value) {
            CommonEvent.DismissLoading -> {}
            CommonEvent.ShowLoading -> {
                FunctionalityNotAvailablePopup {

                }
            }
            //toast不能在这里做
            else -> {}
        }

        DisposableEffect(key1 = true, effect = {
            scope.launch {
                msgEvent.collect {
                    logD("BasePage", it)
                    if (it.isNotEmpty()) {
                        snackHostState.showSnackbar(it)
                    }
                }
            }
            onDispose {

            }
        })
        when (val status = pageStatus.value) {
            is PageStatus.Error -> errorHolder()
            PageStatus.Loading -> loadingHolder()
            PageStatus.None -> {}
            is PageStatus.Success -> {
                content(status.data)
            }
            PageStatus.Empty -> emptyHolder()
            else -> {}
        }
        SnackbarHost(hostState = snackHostState, modifier = Modifier.align(Alignment.Center)) {
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