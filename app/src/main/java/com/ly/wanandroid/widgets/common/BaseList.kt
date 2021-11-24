package com.ly.wanandroid.widgets.common

import androidx.compose.runtime.Composable
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun <T> BaseList(initialData: T? = null) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)



}