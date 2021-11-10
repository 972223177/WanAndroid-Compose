package com.ly.wanandroid.mvi

/**
 * 创建密封类时，实现这个接口
 * 处理view的事件
 */
interface IViewAction

object NoneViewAction : IViewAction

/**
 * 创建密封类时，实现这个接口
 * 消费view的事件
 */
interface IViewEvent

object NoneViewEvent : IViewEvent

sealed class ListViewStatus {
    object None : ListViewStatus()

    object Refreshing : ListViewStatus()

    data class Refreshed<T>(
        val success: Boolean,
        val hasMore: Boolean,
        val data: List<T> = emptyList()
    ) : ListViewStatus()

    object Loading : ListViewStatus()

    data class Loaded<T>(
        val success: Boolean,
        val hasMore: Boolean,
        val data: List<T> = emptyList()
    ) : ListViewStatus()

    data class Empty(val hint: String) : ListViewStatus()
}

sealed class PageStatus {
    object None : PageStatus()

    object Loading : PageStatus()

    data class Success<T>(val data: T) : PageStatus()

    data class Error(val throwable: Throwable) : PageStatus()

}

sealed class CommonEvent {
    data class ShowToast(val msg: String) : CommonEvent()

    object ShowLoading : CommonEvent()

    object DismissLoading : CommonEvent()

}