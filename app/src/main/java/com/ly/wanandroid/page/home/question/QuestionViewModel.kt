package com.ly.wanandroid.page.home.question

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val useCase: HomeUseCase) :
    MviViewModel<NoneViewAction>() {

    val listState = LazyListState()

    val questions = Pager(PagingConfig(pageSize = 20)) {
        QuestionPagingSource(useCase)
    }.flow.cachedIn(viewModelScope)

    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {

    }

}