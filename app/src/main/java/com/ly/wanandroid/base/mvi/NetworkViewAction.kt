package com.ly.wanandroid.base.mvi

/**
 * 创建密封类时，实现这个接口
 * 处理view的事件
 */
interface IViewAction

object NoneViewAction : IViewAction


sealed class PageState {
    object None : PageState()

    object Loading : PageState()

    data class Success(val data: Any?) : PageState()

    data class Error(val msg: String) : PageState()

    object Empty : PageState()

}

sealed class CommonEvent {

    object ShowLoading : CommonEvent()

    object DismissLoading : CommonEvent()

}

sealed class ListViewState<out T : Any> {
    object Init : ListViewState<Nothing>()
    data class Fetched<T : Any>(
        val refresh: Boolean,
        val success: Boolean,
        val hasMore: Boolean,
        val data: T? = null
    ) : ListViewState<T>()

}