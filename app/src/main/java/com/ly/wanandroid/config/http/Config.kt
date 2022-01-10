package com.ly.wanandroid.config.http

import com.ly.wanandroid.config.http.inteceptors.CookieInterceptor
import com.ly.wanandroid.config.http.inteceptors.HeaderInterceptor
import com.ly.wanandroid.base.utils.logD
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


private val client by lazy {
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor { message -> logD(message) })
        .addInterceptor(CookieInterceptor())
        .addInterceptor(HeaderInterceptor())
        .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build()
}

val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://wanandroid.com/")
        .addConverterFactory(JsonConverterFactory())
        .client(client)
        .build()
}

inline fun <reified T> createService(): T = retrofit.create(T::class.java)


@Serializable
data class Response<T>(val data: T?, val errorMsg: String = "", val errorCode: Int = -1) {
    val success: Boolean
        get() = errorCode == 0
}