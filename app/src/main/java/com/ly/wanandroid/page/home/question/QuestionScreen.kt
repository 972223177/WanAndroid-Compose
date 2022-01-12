package com.ly.wanandroid.page.home.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ly.wanandroid.base.widgets.RefreshPagerList
import com.ly.wanandroid.base.widgets.WAppBar
import com.ly.wanandroid.components.ItemArticle

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            WAppBar(title = "问答")
            QuestionList(viewModel = viewModel)
        }
    }
}

@Composable
private fun QuestionList(viewModel: QuestionViewModel) {
    val questions = viewModel.questions.collectAsLazyPagingItems()
    val viewState = remember {
        viewModel.listState
    }
    val listState = if (questions.itemCount > 0) viewState else rememberLazyListState()
    RefreshPagerList(lazyPagingItems = questions, listState = listState) {
        items(questions) { item ->
            if (item != null) {
                ItemArticle(article = item)
            }
        }
    }
}