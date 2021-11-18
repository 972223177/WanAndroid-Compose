package com.ly.wanandroid.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.config.http.handleRequestError
import com.ly.wanandroid.config.http.throwError
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
     * pageRequest(handler = {
     *
     * }){
     *    val response = request()
     *    response
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
        handleError: (FlowCollector<PageData>.(code: Int, msg: String) -> Unit)? = null,
    ): Flow<PageData?> =
        onStart {
            _pageState.value = PageStatus.Loading
        }.onEach {
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

    protected fun <T> Flow<T?>.toPart(
        showErrorToast: Boolean = true,
        showLoading: Boolean = true,
        handleError: (FlowCollector<T>.(code: Int, msg: String) -> Unit)? = null,
        success: suspend (T?) -> Unit,
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

