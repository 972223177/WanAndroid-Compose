package com.ly.wanandroid.config.http

import com.ly.wanandroid.base.utils.logD
import com.ly.wanandroid.base.utils.toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException


private const val TAG = "HttpError"

fun <T> HttpResult<T>.throwError(): HttpResult<T> {
    if (!success) {
        if (errorCode == ErrorStatus.UN_LOGIN) {
            toast(errorMsg)

        } else {
            throw  ApiException(errorMsg, errorCode)
        }

    }
    return this
}

fun <T> Flow<T>.handleRequestError(result: FlowCollector<T>.(code: Int, msg: String) -> Unit): Flow<T> =
    catch { e ->
        if (e is CancellationException) throw e
        val info = handleException(e)
        result(info.first, info.second)
    }

fun handleException(e: Throwable): Pair<Int, String> {
    e.printStackTrace()

    val pair = when (e) {
        is SocketTimeoutException, is ConnectException, is HttpException -> {
            ErrorStatus.NETWORK_ERROR to "网络连接异常"
        }
        is SerializationException, is ParseException -> {
            ErrorStatus.SERVER_ERROR to "数据解析异常"
        }
        is ApiException -> {
            ErrorStatus.SERVER_ERROR to (e.message ?: "")
        }
        is UnknownHostException -> {
            ErrorStatus.NETWORK_ERROR to "网络连接异常"
        }
        is IllegalArgumentException -> {
            ErrorStatus.SERVER_ERROR to "参数错误"
        }
        else -> {
            ErrorStatus.UNKNOWN_ERROR to "未知错误"
        }
    }
    logD(TAG, "code Msg $pair")
    return pair
}

object ErrorStatus {

    const val SUCCESS = 0

    const val TOKEN_INVALID = 401

    const val UNKNOWN_ERROR = 1002

    const val SERVER_ERROR = 1003

    const val NETWORK_ERROR = 1004

    const val API_ERROR = 1005

    const val UN_LOGIN = -1001
}