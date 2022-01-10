package com.ly.wanandroid.base.mvi

/**
 * 创建密封类时，实现这个接口
 * 处理view的事件
 */
interface IViewAction

object NoneViewAction : IViewAction


sealed class PageStatus {
    object None : PageStatus()

    object Loading : PageStatus()

    data class Success(val data: Any?) : PageStatus()

    data class Error(val msg: String) : PageStatus()

    object Empty : PageStatus()

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