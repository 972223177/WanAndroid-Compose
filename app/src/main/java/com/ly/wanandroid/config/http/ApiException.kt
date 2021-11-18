package com.ly.wanandroid.config.http

class ApiException(
    msg: String,
    val code: Int = ErrorStatus.API_ERROR,
) :
    RuntimeException(msg)