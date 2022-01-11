package com.ly.wanandroid.repositories

import com.ly.wanandroid.config.http.HttpResult
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.domain.Navi
import com.ly.wanandroid.domain.Page
import javax.inject.Inject


class ArticleRepository @Inject constructor() {

    suspend fun getTopArticles(): HttpResult<List<Article>> = wanService.getTopArticles()

    suspend fun getArticles(page: Int): HttpResult<Page<Article>> = wanService.getArticles(page)

    suspend fun getQuestions(page: Int): HttpResult<Page<Article>> = wanService.getQuestions(page)

    suspend fun getNavies(): HttpResult<List<Navi>> = wanService.getNavies()

    suspend fun getKnowledgeArticleList(page: Int, id: Int) =
        wanService.getKnowledgeArticleList(page, id)
}