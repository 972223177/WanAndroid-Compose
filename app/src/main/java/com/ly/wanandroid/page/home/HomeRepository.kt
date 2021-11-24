package com.ly.wanandroid.page.home

import com.ly.wanandroid.config.http.Response
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.model.Page
import com.ly.wanandroid.mvi.MviRepository

class HomeRepository {


    suspend fun getBanners(): Response<List<Banner>> = wanService.getBanner()

    suspend fun getArticles(page: Int): Response<Page<Article>> = wanService.getArticles(page)

}