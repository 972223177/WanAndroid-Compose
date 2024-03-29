package com.ly.wanandroid.config.http.inteceptors

import com.ly.wanandroid.base.utils.preference
import com.ly.wanandroid.config.http.HttpConstant
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
            .addHeader("Content-type", "application/json; charset=utf-8")
        val domain = request.url.host
        val url = request.url.toString()
        if (domain.isNotEmpty()) {
            //是否要添加cookie,都是需要登录的接口
            for (method in HttpConstant.verifyCookieMethods) {
                if (url.contains(method)) {
                    val cookie by preference("", key = domain)
                    if (cookie.isNotEmpty()) {
                        builder.addHeader(HttpConstant.COOKIE_NAME, cookie)
                    }
                    break
                }
            }
        }
        return chain.proceed(builder.build())
    }
}