package com.ly.wanandroid.page.home.index

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import com.ly.wanandroid.config.http.throwError
import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviListViewModel
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.page.home.HomeRepository
import com.ly.wanandroid.page.home.model.IndexPageList
import com.ly.wanandroid.utils.logD
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IndexViewModel : MviViewModel<IndexViewAction, List<Banner>>() {
    private val mRepository = HomeRepository()

    val articles = Pager(
        PagingConfig(pageSize = 20)
    ) {
        IndexPagingSource()
    }.flow.cachedIn(viewModelScope)
        .map {
            it.map { wrapper ->
                wrapper.article
            }
        }


    override fun dispatch(viewAction: IndexViewAction) {
        when (viewAction) {
            IndexViewAction.Init -> init()
            IndexViewAction.LoadMore -> {}
            IndexViewAction.Refresh -> {

            }
        }
    }

    private var mInitialed = false
    private fun init() {
        if (mInitialed) return
        viewModelScope.launch {
            request {
                mRepository.getBanners()
            }.toPage(
                success = {
                    mInitialed = true
                }
            ).collect()
        }
    }

}

sealed class IndexViewAction : IViewAction {
    object Init : IndexViewAction()
    object Refresh : IndexViewAction()
    object LoadMore : IndexViewAction()
}
