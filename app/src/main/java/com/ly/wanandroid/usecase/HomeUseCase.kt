package com.ly.wanandroid.usecase

import com.ly.wanandroid.base.mvi.MviUseCase
import com.ly.wanandroid.config.http.throwError
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.repositories.ArticleRepository
import com.ly.wanandroid.repositories.ChapterRepository
import com.ly.wanandroid.repositories.CommonRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class HomeUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val chapterRepository: ChapterRepository,
    private val commonRepository: CommonRepository,
) : MviUseCase(Dispatchers.Default) {
    suspend fun getArticles(page: Int) = request {
        articleRepository.getArticles(page)
    }

    suspend fun getArticlesWithTop(page: Int) = request {
        val totalArticles = mutableListOf<Article>()
        val articles = articleRepository.getArticles(page)
        totalArticles.addAll(articles.data?.datas ?: emptyList())
        if (page == 0) {
            val topResult = articleRepository.getTopArticles().throwError()
            val topArticles = topResult.data?.toMutableList()?.map {
                it.copy(top = true)
            } ?: emptyList()
            totalArticles.addAll(0, topArticles)
        }
        val newPage = articles.data?.copy(datas = totalArticles)
        articles.copy(data = newPage)
    }

    suspend fun getKnowledgeArticleList(page: Int, id: Int) = request {
        articleRepository.getKnowledgeArticleList(page, id)
    }

    suspend fun getQuestions(page: Int) = request {
        articleRepository.getQuestions(page)
    }

    suspend fun getBanners() = request {
        commonRepository.getBanners()
    }

    suspend fun getChapters() = request {
        chapterRepository.getChapters()
    }

    suspend fun getNavies() = request {
        articleRepository.getNavies()
    }
}