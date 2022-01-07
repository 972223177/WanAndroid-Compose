package com.ly.wanandroid.page.home

import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {


    suspend fun getBanners(): Response<List<Banner>> = wanService.getBanner()

    suspend fun getTopArticles(): Response<List<Article>> = wanService.getTopArticles()

    suspend fun getArticles(page: Int): Response<Page<Article>> = wanService.getArticles(page)

    suspend fun getQuestions(page: Int): Response<Page<Article>> = wanService.getQuestions(page)

    suspend fun getKnowledgeTree(): Response<List<Chapter>> = wanService.tree()

    suspend fun getNavies(): Response<List<Navi>> = wanService.getNavies()

}