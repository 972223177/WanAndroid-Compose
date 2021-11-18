package com.ly.wanandroid.config.http

import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.model.InfoTree
import com.ly.wanandroid.model.Page
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val wanService by lazy {
    createService<WanService>()
}

interface WanService {

    @GET("tree/json")
    suspend fun tree(): Response<List<InfoTree>>

    //todo
    @GET("article/list/0/json")
    suspend fun searchByAuthor(): Response<Page<Article>>

    @GET("banner/json")
    suspend fun getBanner(): Response<List<Banner>>
}