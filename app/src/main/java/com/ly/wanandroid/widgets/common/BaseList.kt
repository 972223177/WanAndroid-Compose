package com.ly.wanandroid.widgets.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ly.wanandroid.VoidCallback

@Composable
fun <T : Any> RefreshPagerList(
    lazyPagingItems: LazyPagingItems<T>,
    isRefresh: Boolean = false,
    onRefresh: VoidCallback = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefresh)
    val err = lazyPagingItems.loadState.refresh is LoadState.Error
    if (err) {
        ErrorView {
            lazyPagingItems.retry()
        }
        return
    }
    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        onRefresh()
        lazyPagingItems.refresh()
    }) {
        swipeRefreshState.isRefreshing =
            (lazyPagingItems.loadState.refresh is LoadState.Loading || isRefresh)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            itemContent()
            if (!swipeRefreshState.isRefreshing) {
                item {
                    lazyPagingItems.apply {
                        when (loadState.append) {
                            is LoadState.NotLoading -> {
                                if (loadState.append.endOfPaginationReached) {
                                    NoMoreItem()
                                }
                            }
                            LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem {
                                retry()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingItem() {
    CircularProgressIndicator(modifier = Modifier.size(25.dp))
}

@Composable
private fun ErrorItem(click: VoidCallback) {
    Text(text = "error", modifier = Modifier.clickable(onClick = click))
}

@Composable
private fun NoMoreItem() {
    Text(text = "noMore")
}