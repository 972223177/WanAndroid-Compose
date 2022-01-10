package com.ly.wanandroid.page.home.index

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.ly.wanandroid.R
import com.ly.wanandroid.domain.Banner
import com.ly.wanandroid.components.ItemArticle
import com.ly.wanandroid.ui.theme.onMainOrSurface
import com.ly.wanandroid.base.widgets.Banner
import com.ly.wanandroid.base.widgets.WAppBar
import com.ly.wanandroid.base.widgets.BaseScreen
import com.ly.wanandroid.base.widgets.RefreshPagerList

@Composable
fun IndexScreen(indexViewModel: IndexViewModel = hiltViewModel()) {
    Column {
        WAppBar(
            title = "首页",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_scan),
                        contentDescription = "scan",
                        tint = MaterialTheme.colors.onMainOrSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search",
                        tint = MaterialTheme.colors.onMainOrSurface
                    )
                }
            }
        )
        BaseScreen<List<Banner>>(
            indexViewModel
        ) {
            IndexList(initData = it, viewModel = indexViewModel)
        }
    }
}

@Composable
private fun IndexList(initData: List<Banner>, viewModel: IndexViewModel) {
    val articles = viewModel.articles.collectAsLazyPagingItems()
    val viewState = remember {
        viewModel.listState
    }
    val listState = if (articles.itemCount > 0) viewState else rememberLazyListState()
    RefreshPagerList(lazyPagingItems = articles, listState = listState) {
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