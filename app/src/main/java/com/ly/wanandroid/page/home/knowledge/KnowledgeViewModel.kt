package com.ly.wanandroid.page.home.knowledge

import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.page.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KnowledgeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    MviViewModel<NoneViewAction>() {

    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {

    }
}

