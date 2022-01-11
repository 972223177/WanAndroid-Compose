package com.ly.wanandroid.page.home.knowledge

import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KnowledgeViewModel @Inject constructor(private val useCase: HomeUseCase) :
    MviViewModel<NoneViewAction>() {

    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {

    }
}

