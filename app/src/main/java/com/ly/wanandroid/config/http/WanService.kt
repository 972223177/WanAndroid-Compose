package com.ly.wanandroid.config.http

import com.ly.wanandroid.domain.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val wanService by lazy {
    createService<WanService>()
}

interface WanService {

    @GET("tree/json")
    suspend fun tree(): HttpResult<List<Chapter>>

    @GET("article/list/{pageNum}/json")
    suspend fun getArticles(@Path("pageNum") pageNum: Int): HttpResult<Page<Article>>

    @GET("article/list/{page}/json")
    suspend fun getKnowledgeArticleList(
        @Path("page") page: Int,
        @Query("cid") id: Int
    ): HttpResult<Page<Article>>

    @GET("banner/json")
    suspend fun getBanner(): HttpResult<List<Banner>>

    @GET("article/top/json")
    suspend fun getTopArticles(): HttpResult<List<Article>>

    @GET("wenda/list/{page}/json")
    suspend fun getQuestions(@Path("page") page: Int): HttpResult<Page<Article>>

    @GET("navi/json")
    suspend fun getNavies(): HttpResult<List<Navi>>
}