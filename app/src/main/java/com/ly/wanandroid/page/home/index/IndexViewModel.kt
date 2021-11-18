package com.ly.wanandroid.page.home.index

import androidx.lifecycle.viewModelScope
import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.model.Page
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.page.home.HomeRepository
import com.ly.wanandroid.page.home.model.IndexPageList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class IndexViewModel : MviViewModel<IndexViewAction, IndexPageList>() {
    private val mRepository = HomeRepository()

    override fun dispatch(viewAction: IndexViewAction) {
        when (viewAction) {
            IndexViewAction.Init -> init()
            IndexViewAction.LoadMore -> {}
            IndexViewAction.Refresh -> {}
        }
    }

    private fun init() {
        viewModelScope.launch {
            request {
                mRepository.getBanners()
            }.zip(request { mRepository.getArticles(0) }) { banners, page ->
                IndexPageList(banners ?: emptyList(), page?.datas ?: emptyList())
            }.toPage().collect()
        }
    }

}

sealed class IndexViewAction : IViewAction {
    object Init : IndexViewAction()
    object Refresh : IndexViewAction()
    object LoadMore : IndexViewAction()
}
