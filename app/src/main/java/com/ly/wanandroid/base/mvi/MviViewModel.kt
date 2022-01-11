package com.ly.wanandroid.base.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ly.wanandroid.base.utils.logD
import com.ly.wanandroid.base.utils.toast
import com.ly.wanandroid.config.http.HttpResult
import com.ly.wanandroid.config.http.handleException
import com.ly.wanandroid.config.http.handleRequestError
import kotlinx.coroutines.flow.*

abstract class MviViewModel<A : IViewAction> : ViewModel() {
    protected val _pageState = MutableStateFlow<PageState>(PageState.None)

    val pageState: StateFlow<PageState> = _pageState

    protected val _commonEvent = SingleLiveEvent<CommonEvent>()

    val commonEvent: LiveData<CommonEvent> = _commonEvent

    protected var mInitialed = false

    abstract fun dispatch(viewAction: A)

    abstract fun onFirstInit()

    fun initial() {
        if (mInitialed) return
        mInitialed = true
        onFirstInit()
    }

    /**
     * request{
     *     repository.getData()
     * }
     */
    protected inline fun <T> wrapFlow(
        crossinline block: suspend () -> HttpResult<T>
    ): Flow<T?> = flow {
        val result = block()
        emit(result.data)
    }


    protected suspend fun <PageData> Flow<PageData?>.toPage(
        showErrorToast: Boolean = true,
        success: ((PageData?) -> Unit)? = null,
        handleError: (FlowCollector<PageData>.(code: Int, msg: String) -> Unit)? = null,
    ) =
        onStart {
            _pageState.value = PageState.Loading
        }.onEach {
            success?.invoke(it)
            _pageState.value = if (it == null) {
                PageState.Empty
            } else {
                PageState.Success(it)
            }
        }.handleRequestError { code, msg ->
            if (handleError != null) {
                handleError(code, msg)
            }
            if (showErrorToast) {
                toast(msg)
            }
            _pageState.value = PageState.Error(msg)
        }.collect()

    protected suspend inline fun <T> Flow<T?>.toPart(
        showErrorToast: Boolean = true,
        showLoading: Boolean = true,
        noinline handleError: (FlowCollector<T>.(code: Int, msg: String) -> Unit)? = null,
        crossinline success: suspend (T?) -> Unit,
    ) = onStart {
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
            toast(msg)
        }
    }.onCompletion {
        if (showLoading) {
            _commonEvent.setValue(CommonEvent.DismissLoading)
        }
    }.collect()

    protected inline fun <T> pageRequest(
        handleError: (Int, String) -> String = { _, msg -> msg },
        requestBlock: () -> T?
    ) {
        _pageState.value = PageState.Loading
        try {
            val result = requestBlock()
            if (result == null) {
                _pageState.value = PageState.Empty
            } else {
                _pageState.value = PageState.Success(result)
            }
        } catch (e: Exception) {
            val (code, msg) = handleException(e)
            _pageState.value = PageState.Error(handleError(code, msg))
        }
    }

    protected inline fun partRequest(
        showLoading: Boolean = true,
        showErrorToast: Boolean = true,
        handleError: (Int, String) -> Unit = { _, _ -> },
        requestBlock: () -> Unit
    ) {
        if (showLoading) {
            _commonEvent.setValue(CommonEvent.ShowLoading)
        }
        try {
            requestBlock()
        } catch (e: Exception) {
            val (code, msg) = handleException(e)
            handleError(code, msg)
            if (showErrorToast) {
                toast(msg)
            }
        } finally {
            if (showLoading) {
                _commonEvent.setValue(CommonEvent.DismissLoading)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        logD("${this::class.java.simpleName}:clear")
    }

}

abstract class MviListViewModel<A : IViewAction, ListViewData : Any> :
    MviViewModel<A>() {
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