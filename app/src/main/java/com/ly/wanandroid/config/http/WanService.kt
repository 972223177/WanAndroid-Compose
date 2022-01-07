package com.ly.wanandroid.config.http

import com.ly.wanandroid.model.*
import retrofit2.http.GET
import retrofit2.http.Path

val wanService by lazy {
    createService<WanService>()
}

interface WanService {

    @GET("tree/json")
    suspend fun tree(): Response<List<Chapter>>

    @GET("article/list/{pageNum}/json")
    suspend fun getArticles(@Path("pageNum") pageNum: Int): Response<Page<Article>>

    @GET("banner/json")
    suspend fun getBanner(): Response<List<Banner>>

    @GET("article/top/json")
    suspend fun getTopArticles(): Response<List<Article>>

    @GET("wenda/list/{page}/json")
    suspend fun getQuestions(@Path("page") page: Int): Response<Page<Article>>

    @GET("navi/json")
    suspend fun getNavies(): Response<List<Navi>>
}