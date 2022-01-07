package com.ly.wanandroid.page.home.knowledge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.onMainOrSurface
import com.ly.wanandroid.ui.theme.onMainOrSurfaceAlpha
import com.ly.wanandroid.widgets.WAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun KnowledgeScreen(viewModel: KnowledgeViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            val scope = rememberCoroutineScope()
            val pagerState = rememberPagerState()
            LaunchedEffect(key1 = Unit, block = {
                viewModel.dispatch(KnowledgeViewAction.Init)
            })
            WAppBar(title = {
                Row {
                    KnowledgeTab(title = "体系", selected = pagerState.targetPage == 0) {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                    KnowledgeTab(title = "导航", selected = pagerState.targetPage == 1) {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                }
            })
            HorizontalPager(count = 2, state = pagerState) { index ->
                SubScreen(index = index, viewModel)
            }
        }
    }
}

@Composable
private fun KnowledgeTab(title: String, selected: Boolean, clickable: VoidCallback) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.W500,
        color = if (selected) MaterialTheme.colors.onMainOrSurface else MaterialTheme.colors.onMainOrSurfaceAlpha,
        modifier = Modifier
            .clickable { clickable() }
            .padding(horizontal = 8.dp, vertical = 5.dp)
    )
}

@Composable
private fun SubScreen(index: Int, viewModel: KnowledgeViewModel) {

}

@Preview
@Composable
fun KnowledgePre() {
    KnowledgeScreen()
}