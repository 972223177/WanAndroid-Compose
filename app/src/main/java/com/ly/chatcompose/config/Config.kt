package com.ly.chatcompose.config

import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


private val client by lazy {
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor {
            val request = it.request()
            println(
                "************${request.headers}************\n" +
                        "* method:${request.method}\n" +
                        "* url:${request.url}\n"+
                        "* requestBody:${request.body}\n" +
                        "********************************************\n"
            )
            return@addInterceptor it.proceed(request)
        }
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
data class Response<T>(val data: T?, val errorMsg: String = "", val errorCode: Int = -1)