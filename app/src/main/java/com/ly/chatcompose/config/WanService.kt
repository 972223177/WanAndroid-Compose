package com.ly.chatcompose.config

import com.ly.chatcompose.model.Article
import com.ly.chatcompose.model.Banner
import com.ly.chatcompose.model.InfoTree
import com.ly.chatcompose.model.Page
import retrofit2.http.GET
import retrofit2.http.Query

interface WanService {

    @GET("tree/json")
    suspend fun tree(): Response<List<InfoTree>>

    @GET("article/list/0/json")
    suspend fun searchByAuthor(@Query("author") author: String): Response<Page<Article>>

    @GET("banner/json")
    suspend fun getBanner(): Response<List<Banner>>
}