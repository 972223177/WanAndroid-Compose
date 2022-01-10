package com.ly.wanandroid.page.home.knowledge

import androidx.lifecycle.viewModelScope
import com.ly.wanandroid.mvi.IViewAction
import com.ly.wanandroid.mvi.MviViewModel
import com.ly.wanandroid.mvi.NoneViewAction
import com.ly.wanandroid.page.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaviViewModel @Inject constructor(private val repository: HomeRepository) :
    MviViewModel<NoneViewAction>() {
    override fun dispatch(viewAction: NoneViewAction) {

    }

    override fun onFirstInit() {
        viewModelScope.launch {
            request {
                repository.getNavies()
            }.toPage(showErrorToast = true)
        }
    }
}

sealed class NaviViewAction : IViewAction {

}