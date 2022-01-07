package com.ly.wanandroid.page.home.index

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.page.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(private val mRepository: HomeRepository) :
    MviViewModel<IndexViewAction>() {

    val articles = Pager(
        PagingConfig(pageSize = 20)
    ) {
        IndexPagingSource(mRepository)
    }.flow.cachedIn(viewModelScope)

    val listState = LazyListState()

    override fun dispatch(viewAction: IndexViewAction) {
        when (viewAction) {
            IndexViewAction.Init -> init {
                getPageData()
            }
            IndexViewAction.LoadMore -> {}
            IndexViewAction.Refresh -> {

            }
        }
    }

    private fun getPageData() {
        viewModelScope.launch {
            request {
                mRepository.getBanners()
            }.toPage().collect()
        }
    }

}

sealed class IndexViewAction : IViewAction {
    object Init : IndexViewAction()
    object Refresh : IndexViewAction()
    object LoadMore : IndexViewAction()
}
