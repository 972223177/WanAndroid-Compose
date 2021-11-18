@file:OptIn(ExperimentalPagerApi::class)

package com.ly.wanandroid.page.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountTree
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Message
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.page.home.index.IndexScreen
import com.ly.wanandroid.page.home.knowledge.KnowledgeScreen
import com.ly.wanandroid.page.home.mine.MineScreen
import com.ly.wanandroid.page.home.question.QuestionScreen
import kotlinx.coroutines.launch


private val titles = listOf("首页", "问答", "体系", "我的")
private val icons =
    listOf(
        Icons.Sharp.Home,
        Icons.Sharp.Message,
        Icons.Sharp.AccountTree,
        Icons.Sharp.Person
    )


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
                0 -> IndexScreen()
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
        icons.forEachIndexed { index, imageVector ->
            val route = titles[index]

            BottomNavigationItem(selected = currentPage == index, icon = {
                Icon(imageVector = imageVector, contentDescription = imageVector.name)
            }, label = {
                Text(text = route)
            }, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            })
        }
    }
}

@Composable
private fun Screen(route: String, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var counter by remember {
            mutableStateOf(0)
        }
        Text(text = "$route $counter", modifier = Modifier.align(Alignment.Center))
        Button(onClick = {
            counter += 1
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Text(text = "add")
        }
    }
}
