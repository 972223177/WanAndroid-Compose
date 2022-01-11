package com.ly.wanandroid.base.mvi

import com.ly.wanandroid.config.http.HttpResult
import com.ly.wanandroid.config.http.throwError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class MviUseCase(val dispatcher: CoroutineDispatcher) {

    protected suspend inline fun <T> request(crossinline block: suspend () -> HttpResult<T>): HttpResult<T> =
        withContext(dispatcher) {
            block().throwError()
        }
}