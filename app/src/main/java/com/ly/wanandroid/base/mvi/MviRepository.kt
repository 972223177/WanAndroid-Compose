package com.ly.wanandroid.base.mvi

import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.config.http.handleException
import com.ly.wanandroid.config.http.wanService
import kotlinx.coroutines.CancellationException

abstract class MviRepository {
    protected val mWanService by lazy(LazyThreadSafetyMode.NONE) {
        wanService
    }

    inline fun <T> requestWanService(block: () -> Response<T>): Response<T> {
        return try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw  e
            }
            val info = handleException(e)
            Response(null, info.second, info.first)
        }

    }
}