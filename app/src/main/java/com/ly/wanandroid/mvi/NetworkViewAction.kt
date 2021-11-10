package com.ly.wanandroid.mvi

sealed class NetworkViewAction

object PageRequest : NetworkViewAction()

object PartRequest : NetworkViewAction()

object MultiRequest : NetworkViewAction()

object ErrorRequest : NetworkViewAction()


sealed class PageStatus

object Loading : PageStatus()

object Success : PageStatus()

data class Empty(val hint: String) : PageStatus()

data class Error(val throwable: Throwable) : PageStatus()


sealed class NetworkViewEvent

data class ShowToast(val msg: String) : NetworkViewEvent()

object ShowLoading : NetworkViewEvent()

object DismissLoading : NetworkViewEvent()
