package com.ly.chatcompose.config

class ApiException(val code: Int = ErrorStatus.API_ERROR, throwable: Throwable) :
    RuntimeException(throwable)