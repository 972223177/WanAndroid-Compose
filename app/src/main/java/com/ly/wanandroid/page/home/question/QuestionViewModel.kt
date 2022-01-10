package com.ly.wanandroid.page.home.question

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.page.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    MviViewModel<QuestionViewAction>() {

    val listState = LazyListState()

    val questions = Pager(PagingConfig(pageSize = 20)) {
        QuestionPagingSource(homeRepository)
    }.flow.cachedIn(viewModelScope)

    override fun dispatch(viewAction: QuestionViewAction) {

    }

    override fun onFirstInit() {

    }

}

sealed class QuestionViewAction : IViewAction {

}