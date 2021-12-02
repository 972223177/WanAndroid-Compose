package com.ly.wanandroid.page.home.index

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.page.widgets.ItemArticle
import com.ly.wanandroid.widgets.Banner
import com.ly.wanandroid.widgets.common.BaseScreen

@Composable
fun IndexScreen() {
    val indexViewModel = viewModel<IndexViewModel>()

    LaunchedEffect(key1 = false) {
        indexViewModel.dispatch(IndexViewAction.Init)
    }
    BaseScreen(
        pageStatus = indexViewModel.pageState.observeAsState(),
        commonEvent = indexViewModel.commonEvent.observeAsState(),
        msgEvent = indexViewModel.msgEvent
    ) {
        IndexList(initData = it, viewModel = indexViewModel)
    }
}

@Composable
private fun IndexList(initData: List<Banner>, viewModel: IndexViewModel) {
    val articles = viewModel.articles.collectAsLazyPagingItems()
    val refreshState =
        rememberSwipeRefreshState(isRefreshing = articles.loadState.refresh is LoadState.Loading)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        SwipeRefresh(state = refreshState,
            onRefresh = {
                articles.refresh()
                refreshState.isRefreshing = true
            }) {

            LazyColumn {
                item {
                    IndexBanner(initData)
                }
                items(articles) { item ->
                    if (item != null) {
                        ItemArticle(article = item)
                    }
                }

            }
        }
    }
}


@Composable
private fun IndexBanner(banners: List<Banner>) {
    val visibleBanners = remember(banners) {
        banners.filter {
            it.isVisible == 1
        }
    }
    val configuration = LocalConfiguration.current
    val height = minOf(
        configuration.screenWidthDp,
        configuration.screenHeightDp
    ) * (9.0f / 16.0f)
    Banner(
        itemCount = visibleBanners.size, modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        val banner = visibleBanners[it]
        val imagePainter = rememberImagePainter(data = banner.imagePath)
        Image(
            painter = imagePainter,
            contentDescription = banner.desc,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
        )
    }

}