package com.ly.wanandroid.page.chapter

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.route.ChapterRouteArg
import com.ly.wanandroid.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(private val useCase: HomeUseCase) :
    MviViewModel<NoneViewAction>() {
    private var mRouteArg: ChapterRouteArg? = null

    private val mTabPagingData = mutableListOf<Flow<PagingData<Article>>>()

    private val mTabListState = mutableListOf<LazyListState>()

    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {
        if (mRouteArg == null) {
            mInitialed = false
            return
        }
        pageRequest {
            mRouteArg
        }
    }

    fun getPagingData(index: Int) = mTabPagingData[index]

    fun getListState(index: Int) = mTabListState[index]


    fun setChapter(routeArg: ChapterRouteArg?) {
        if (mRouteArg != null || routeArg == mRouteArg) return
        mRouteArg = routeArg
        mTabPagingData.clear()
        mTabListState.clear()
        mRouteArg?.chapter?.children?.forEach {
            mTabPagingData.add(
                Pager(
                    config = PagingConfig(
                        pageSize = 20
                    )
                ) {
                    ChapterTabPagingSource(useCase, it.id)
                }.flow.cachedIn(viewModelScope)
            )
            mTabListState.add(LazyListState())
        }
    }
}