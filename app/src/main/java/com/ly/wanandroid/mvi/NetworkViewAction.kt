package com.ly.wanandroid.mvi

/**
 * 创建密封类时，实现这个接口
 * 处理view的事件
 */
interface IViewAction

object NoneViewAction : IViewAction


sealed class PageStatus<out T : Any> {
    object None : PageStatus<Nothing>()

    object Loading : PageStatus<Nothing>()

    data class Success<T : Any>(val data: T) : PageStatus<T>()

    data class Error(val msg: String) : PageStatus<Nothing>()

    object Empty : PageStatus<Nothing>()

}

sealed class CommonEvent {
    data class ShowToast(val msg: String) : CommonEvent()

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