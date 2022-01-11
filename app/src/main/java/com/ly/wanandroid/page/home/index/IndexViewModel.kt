package com.ly.wanandroid.page.home.index

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(private val useCase: HomeUseCase) :
    MviViewModel<NoneViewAction>() {

    val articles = Pager(
        PagingConfig(pageSize = 20)
    ) {
        IndexPagingSource(useCase)
    }.flow.cachedIn(viewModelScope)

    val listState = LazyListState()

    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {
        viewModelScope.launch {
            pageRequest {
                useCase.getBanners().data
            }
        }
    }

}
