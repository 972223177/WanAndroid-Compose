package com.ly.wanandroid.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.wanandroid.utils.toast
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//todo multi type request
abstract class MviViewModel<A : IViewAction, E : IViewEvent> : ViewModel() {
    protected val _pageState = MutableLiveData<PageStatus>(PageStatus.None)

    val pageState: LiveData<PageStatus> = _pageState

    protected val _commonEvent = SingleLiveEvent<CommonEvent>()

    val commonEvent: LiveData<CommonEvent> = _commonEvent

    protected val _viewEvent = MutableLiveData<E>()

    val viewEvent: LiveData<E> = _viewEvent

    abstract fun dispatch(viewAction: A)

    /**
     * pageRequest{
     *    val result = request()
     *    emit(result)
     * }
     * @param showErrorToast show toast when throw throwable
     */
    private fun <T> pageRequest(
        showErrorToast: Boolean = true,
        block: suspend FlowCollector<T>.() -> Unit
    ) {
        viewModelScope.launch {
            flow(block)
                .onStart {
                    _pageState.value = PageStatus.Loading
                }.onEach {
                    _pageState.value = PageStatus.Success(it)
                }.catch { error ->
                    if (showErrorToast) {
                        toast(error.message ?: "")
                    }
                    _pageState.value = PageStatus.Error(error)
                }.collect()
        }
    }

    /**
     * partRequest(block = {
     *      emit(result)
     *  }){
     *     _viewEvent.value = ViewEvent(it)
     *  }
     */
    private fun <T> partRequest(
        showLoading: Boolean = true,
        showErrorToast: Boolean = false,
        block: suspend FlowCollector<T>.() -> Unit,
        success: suspend (T) -> Unit
    ) {
        viewModelScope.launch {
            flow(block)
                .onStart {
                    if (showLoading) {
                        _commonEvent.setEvent(CommonEvent.ShowLoading)
                    }
                }.onEach(success)
                .catch { error ->
                    if (showErrorToast) {
                        toast(error.message ?: "")
                    }
                }
                .onCompletion {
                    if (showLoading) {
                        _commonEvent.setEvent(CommonEvent.DismissLoading)
                    }
                }.collect()
        }
    }

}

abstract class MviListViewModel<A : IViewAction, E : IViewEvent> : MviViewModel<A, E>() {
    protected val _listViewState = MutableLiveData<ListViewStatus>(ListViewStatus.None)

    val listViewState: LiveData<ListViewStatus> = _listViewState

    /**
     * fetchList(refresh = true){
     *      val fetchResult = request()
     *      val hasMore = fetchResult.hasMore
     *      val list = fetchResult.list
     *      emit(hasMore,list)
     * }
     * emit Pair(hasMore,List<T>)
     */
    private fun <T> fetchList(
        refresh: Boolean = true,
        showErrorToast: Boolean = false,
        block: suspend FlowCollector<Pair<Boolean, List<T>>>.() -> Unit
    ) {
        viewModelScope.launch {
            flow(block)
                .onStart {
                    _listViewState.value =
                        if (refresh) ListViewStatus.Refreshing else ListViewStatus.Loading
                }.onEach {
                    _listViewState.value = if (refresh)
                        ListViewStatus.Refreshed(true, it.first, it.second)
                    else
                        ListViewStatus.Loaded(true, it.first, it.second)
                }.catch { error ->
                    if (showErrorToast) {
                        toast(error.message ?: "")
                    }
                    _listViewState.value = if (refresh)
                        ListViewStatus.Refreshed<T>(false, hasMore = false)
                    else
                        ListViewStatus.Loaded<T>(true, hasMore = false)
                }
        }
    }
}
