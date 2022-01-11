package com.ly.wanandroid.page.home.knowledge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.base.widgets.BaseScreen
import com.ly.wanandroid.base.widgets.WAppBar
import com.ly.wanandroid.components.TabItem
import com.ly.wanandroid.domain.Chapter
import com.ly.wanandroid.domain.Navi
import com.ly.wanandroid.route.goChapter
import com.ly.wanandroid.route.goWebView
import com.ly.wanandroid.ui.theme.surface1Top
import com.ly.wanandroid.ui.theme.textSecond
import com.ly.wanandroid.ui.theme.textSurface
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun KnowledgeScreen(viewModel: KnowledgeViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState()
            WAppBar(title = {
                Row {
                    TabItem(title = "体系", selected = pagerState.targetPage == 0) {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                    TabItem(title = "导航", selected = pagerState.targetPage == 1) {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                }
            })
            HorizontalPager(count = 2, state = pagerState) { index ->
                if (index == 0) {
                    ChapterTabScreen()
                } else {
                    NaviTabScreen()
                }
            }
        }
    }
}

@Composable
private fun ChapterTabScreen() {
    val viewModel: ChaptersListTabViewModel = hiltViewModel()
    BaseScreen<List<Chapter>>(viewModel = viewModel) {
        val navController = LocalNavController.current
        LazyColumn(
            modifier = Modifier.padding(
                top = 5.dp,
                start = 11.dp,
                end = 11.dp,
                bottom = 11.dp
            )
        ) {
            items(it) { item ->
                Title(text = item.name)
                FlowRow(
                    mainAxisSpacing = 10.dp,
                    crossAxisSpacing = 10.dp,
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    item.children.forEachIndexed { index, chapter ->
                        SubTitle(text = chapter.name) {
                            navController.goChapter(index, item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NaviTabScreen() {
    val viewModel: NaviListTabViewModel = hiltViewModel()
    BaseScreen<List<Navi>>(viewModel = viewModel) {
        val navController = LocalNavController.current
        LazyColumn(
            modifier = Modifier.padding(
                top = 5.dp,
                start = 11.dp,
                end = 11.dp,
                bottom = 11.dp
            )
        ) {
            items(it) { item ->
                Title(text = item.name)
                FlowRow(
                    mainAxisSpacing = 10.dp,
                    crossAxisSpacing = 10.dp,
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    item.articles.forEach {
                        SubTitle(text = it.title) {
                            navController.goWebView(it.link)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Title(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        color = MaterialTheme.colors.textSecond,
        modifier = Modifier.padding(
            top = 16.dp, bottom = 5.dp
        )
    )
}

@Composable
private fun SubTitle(text: String, clickable: VoidCallback) {
    Text(
        text = text,
        fontSize = 13.sp,
        color = MaterialTheme.colors.textSurface,
        modifier = Modifier
            .clickable { clickable() }
            .background(color = MaterialTheme.colors.surface1Top, CircleShape)
            .padding(horizontal = 15.dp, vertical = 5.dp)
    )
}


@Preview
@Composable
fun KnowledgePre() {
    KnowledgeScreen()
}