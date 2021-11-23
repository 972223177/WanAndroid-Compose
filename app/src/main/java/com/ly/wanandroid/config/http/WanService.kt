package com.ly.wanandroid.config.http

import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.model.Chapter
import com.ly.wanandroid.model.Page
import retrofit2.http.GET
import retrofit2.http.Path

val wanService by lazy {
    createService<WanService>()
}

interface WanService {

    @GET("tree/json")
    suspend fun tree(): Response<List<Chapter>>

    //todo
    @GET("article/list/{pageNum}/json")
    suspend fun getArticles(@Path("pageNum") pageNum:Int): Response<Page<Article>>

    @GET("banner/json")
    suspend fun getBanner(): Response<List<Banner>>

    @GET("article/top/json")
    suspend fun getTopArticles():Response<Page<Article>>
}