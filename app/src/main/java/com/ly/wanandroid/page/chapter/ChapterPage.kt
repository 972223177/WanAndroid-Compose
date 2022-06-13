package com.ly.wanandroid.page.chapter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.base.utils.logD
import com.ly.wanandroid.base.widgets.AppBarTitle
import com.ly.wanandroid.base.widgets.BaseScreen
import com.ly.wanandroid.base.widgets.RefreshPagerList
import com.ly.wanandroid.components.ItemArticle
import com.ly.wanandroid.components.TabItem
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.route.ChapterRouteArg
import com.ly.wanandroid.theme.mainOrSurface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ChapterPage(viewModel: ChapterViewModel) {
    BaseScreen<ChapterRouteArg>(viewModel = viewModel) {
        val scope = rememberCoroutineScope()
        val tabState = rememberLazyListState()
        val pagerState = rememberPagerState(it.targetIndex)
        Column {
            LaunchedEffect(key1 = pagerState) {
                snapshotFlow {
                    pagerState.targetPage
                }.collectLatest { page ->
                    tabState.animateScrollToItem(page)
                }
            }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.mainOrSurface)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppBarTitle(title = it.chapter.name)
                LazyRow(
                    state = tabState,
                    modifier = Modifier
                        .background(MaterialTheme.colors.mainOrSurface)
                        .padding(5.dp)
                ) {
                    itemsIndexed(it.chapter.children) { index, item ->
                        TabItem(
                            title = item.name,
                            selected = pagerState.targetPage == index
                        ) {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    }
                }
            }
            HorizontalPager(
                count = it.chapter.children.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                KnowledgeList(
                    viewModel.getPagingData(page).collectAsLazyPagingItems(),
                    viewModel.getListState(page)
                )
            }
        }
    }
}

@Composable
private fun KnowledgeList(items: LazyPagingItems<Article>, listState: LazyListState) {
    logD("knowledgeList ${listState.hashCode()}")
    RefreshPagerList(lazyPagingItems = items, listState = listState) {
        items(items) { item ->
            if (item != null) {
                ItemArticle(article = item)
            }
        }
    }
}
