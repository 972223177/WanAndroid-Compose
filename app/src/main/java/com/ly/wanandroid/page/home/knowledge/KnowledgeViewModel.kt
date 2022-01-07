package com.ly.wanandroid.page.home.knowledge

import androidx.lifecycle.viewModelScope
import com.ly.wanandroid.model.Chapter
import com.ly.wanandroid.model.Navi
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.page.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnowledgeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    MviViewModel<KnowledgeViewAction>() {

    private val mChaptersFlow = MutableStateFlow<List<Chapter>>(emptyList())
    val chaptersFlow: StateFlow<List<Chapter>> = mChaptersFlow
    private val mNaviFlow = MutableStateFlow<List<Navi>>(emptyList())
    val naviFlow: StateFlow<List<Navi>> = mNaviFlow

    override fun dispatch(viewAction: KnowledgeViewAction) {
        when (viewAction) {
            KnowledgeViewAction.Init -> init {
                getPageData()
            }
        }
    }

    private fun getPageData() {
        viewModelScope.launch {
            request {
                homeRepository.getKnowledgeTree()
            }.toPart {
                mChaptersFlow.value = it ?: emptyList()
            }
            request {
                homeRepository.getNavies()
            }.toPart {
                mNaviFlow.value = it ?: emptyList()
            }
        }
    }
}


sealed class KnowledgeViewAction : IViewAction {
    object Init : KnowledgeViewAction()
}