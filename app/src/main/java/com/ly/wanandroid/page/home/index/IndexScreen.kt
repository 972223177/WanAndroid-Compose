package com.ly.wanandroid.page.home.index

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.page.home.model.IndexPageList
import com.ly.wanandroid.widgets.Banner
import com.ly.wanandroid.widgets.common.BaseScreen

@Composable
fun IndexScreen() {
    val indexViewModel = viewModel<IndexViewModel>()

    LaunchedEffect(false) {
        indexViewModel.dispatch(IndexViewAction.Init)
    }
    BaseScreen(
        pageStatus = indexViewModel.pageState.observeAsState(),
        commonEvent = indexViewModel.commonEvent.observeAsState(),
        msgEvent = indexViewModel.msgEvent
    ) {
        IndexList(data = it, viewModel = indexViewModel)
    }
}

@Composable
private fun IndexList(data: IndexPageList, viewModel: IndexViewModel) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        SwipeRefresh(state = refreshState, onRefresh = {
            viewModel.dispatch(IndexViewAction.Refresh)
            refreshState.isRefreshing = true
        }) {
            LazyColumn {
                item {
                    IndexBanner(data.banners)
                }
//                items(items = data.articles) { item ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        ) {
//                            Text(text = item.author)
//                            Text(text = item.title)
//                        }
//                    }
//                }

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
    Banner(
        itemCount = visibleBanners.size, modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
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