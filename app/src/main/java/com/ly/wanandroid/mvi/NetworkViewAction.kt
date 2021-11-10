package com.ly.wanandroid.mvi

interface IViewAction

object NoneViewAction : IViewAction

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

sealed class NetworkViewEvent {
    data class ShowToast(val msg: String) : NetworkViewEvent()

    object ShowLoading : NetworkViewEvent()

    object DismissLoading : NetworkViewEvent()

}