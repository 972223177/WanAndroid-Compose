package com.ly.wanandroid.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.config.http.handleRequestError
import com.ly.wanandroid.config.http.throwError
import com.ly.wanandroid.utils.logD
import kotlinx.coroutines.flow.*

abstract class MviViewModel<A : IViewAction, PageData : Any> : ViewModel() {
    protected val _pageState = MutableLiveData<PageStatus<PageData>>(PageStatus.None)

    val pageState: LiveData<PageStatus<PageData>> = _pageState

    protected val _commonEvent = SingleLiveEvent<CommonEvent>()

    protected val _msgEvent = MutableStateFlow("")
    val msgEvent: StateFlow<String> = _msgEvent

    val commonEvent: LiveData<CommonEvent> = _commonEvent

    abstract fun dispatch(viewAction: A)

    /**
     * request{
     *     repository.getData()
     * }
     */
    protected inline fun <T> request(
        crossinline block: suspend () -> Response<T>
    ): Flow<T?> = flow {
        val result = block().throwError()
        emit(result.data)
    }


    protected fun Flow<PageData?>.toPage(
        showErrorToast: Boolean = true,
        success: ((PageData?) -> Unit)? = null,
        handleError: (FlowCollector<PageData>.(code: Int, msg: String) -> Unit)? = null,
    ): Flow<PageData?> =
        onStart {
            _pageState.value = PageStatus.Loading
        }.onEach {
            success?.invoke(it)
            _pageState.value = if (it == null) {
                PageStatus.Empty
            } else {
                PageStatus.Success(it)
            }
        }.handleRequestError { code, msg ->
            if (handleError != null) {
                handleError(code, msg)
            }
            if (showErrorToast) {
                _msgEvent.value = msg
            }
            _pageState.value = PageStatus.Error(msg)
        }

    protected inline fun <T> Flow<T?>.toPart(
        showErrorToast: Boolean = true,
        showLoading: Boolean = true,
        noinline handleError: (FlowCollector<T>.(code: Int, msg: String) -> Unit)? = null,
        crossinline success: suspend (T?) -> Unit,
    ): Flow<T?> = onStart {
        if (showLoading) {
            _commonEvent.setValue(CommonEvent.ShowLoading)
        }
    }.onEach {
        success(it)
    }.handleRequestError { code, msg ->
        if (handleError != null) {
            handleError(code, msg)
        }
        if (showErrorToast) {
            _msgEvent.value = msg
        }
    }.onCompletion {
        if (showLoading) {
            _commonEvent.setValue(CommonEvent.DismissLoading)
        }
    }

}

abstract class MviListViewModel<A : IViewAction, PageData : Any, ListViewData : Any> :
    MviViewModel<A, PageData>() {
    private val mListViewState = MutableStateFlow<ListViewState<ListViewData>>(ListViewState.Init)
    val listViewState: StateFlow<ListViewState<ListViewData>> = mListViewState
    protected val mIsRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = mIsRefresh

    protected var mPage = 1

    /**
     *  fetchList(refresh = true){ page->
     *      request{
     *        val result = repository.getList(page)
     *        result.hasMore to result.data
     *      }
     *  }
     */
    protected suspend fun fetchList(
        refresh: Boolean,
        fetch: suspend (page: Int) -> Flow<Pair<Boolean, ListViewData>>
    ) {
        mPage = if (refresh) 1 else mPage + 1
        if (refresh) {
            mIsRefresh.value = true
        }
        fetch(mPage).onEach {
            mListViewState.value = ListViewState.Fetched(refresh, true, it.first, it.second)
        }.handleRequestError { code, msg ->
            logD("fetchListError(refresh:$refresh) -> code:$code -> msg:$msg")
            mListViewState.value = ListViewState.Fetched(
                refresh = refresh,
                success = false,
                hasMore = false,
                data = null
            )
        }.onCompletion {
            mIsRefresh.value = false
        }.collect()
    }
}