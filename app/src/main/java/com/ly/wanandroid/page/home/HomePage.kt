@file:OptIn(ExperimentalPagerApi::class)

package com.ly.wanandroid.page.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.R
import com.ly.wanandroid.page.home.index.IndexScreen
import com.ly.wanandroid.page.home.knowledge.KnowledgeScreen
import com.ly.wanandroid.page.home.mine.MineScreen
import com.ly.wanandroid.page.home.question.QuestionScreen
import kotlinx.coroutines.launch


private val titles = listOf("首页", "问答", "体系", "我的")

@Composable
fun HomePage() {
    val pageState = rememberPagerState()
    Scaffold(
        bottomBar = {
            BottomNav(pageState)
        },
    ) {
        HorizontalPager(
            count = titles.size,
            state = pageState,
            modifier = Modifier.padding(it)
        ) { page ->
            when (page) {
                0 -> {
                    IndexScreen()
                }
                1 -> QuestionScreen()
                2 -> KnowledgeScreen()
                else -> MineScreen()
            }
        }
    }
}

@Composable
private fun BottomNav(pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        val currentPage = pagerState.targetPage
        titles.forEachIndexed { index, title ->
            val iconId = when (index) {
                0 -> R.drawable.ic_bottom_bar_home
                1 -> R.drawable.ic_bottom_bar_ques
                2 -> R.drawable.ic_bottom_bar_navi
                else -> R.drawable.ic_bottom_bar_mine
            }
            BottomNavigationItem(selected = currentPage == index, icon = {
                Icon(
                    painter = painterResource(id = iconId),
                    modifier = Modifier.size(25.dp),
                    contentDescription = title
                )
            }, label = {
                Text(text = title)
            }, onClick = {
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            })
        }
    }
}