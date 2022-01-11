package com.ly.wanandroid.page.home.knowledge

import androidx.lifecycle.viewModelScope
import com.ly.wanandroid.base.mvi.IViewAction
import com.ly.wanandroid.base.mvi.MviViewModel
import com.ly.wanandroid.base.mvi.NoneViewAction
import com.ly.wanandroid.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaviListTabViewModel @Inject constructor(private val useCase: HomeUseCase) :
    MviViewModel<NoneViewAction>() {
    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {
        viewModelScope.launch {
            wrapFlow {
                useCase.getNavies()
            }.toPage(showErrorToast = true)
        }
    }
}

sealed class NaviViewAction : IViewAction {

}