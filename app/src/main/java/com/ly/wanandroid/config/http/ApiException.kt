package com.ly.wanandroid.config.http

open class ApiException(
    msg: String,
    val code: Int = ErrorStatus.API_ERROR,
) :
    RuntimeException(msg)

class UnLoginException(
    msg: String
) : ApiException(msg, code = ErrorStatus.UN_LOGIN)