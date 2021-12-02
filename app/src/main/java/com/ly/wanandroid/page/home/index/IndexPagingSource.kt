package com.ly.wanandroid.page.home.index

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ly.wanandroid.config.http.throwError
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.model.Article
import java.lang.Exception

class IndexPagingSource : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? =
        state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = if (nextPageNumber == 1) {
                val topArticle = wanService.getTopArticles().data ?: emptyList()
                val articles = wanService.getArticles(nextPageNumber)
                val newArticles = mutableListOf<Article>().also {
                    it.addAll(topArticle)
                    it.addAll(articles.data?.datas ?: emptyList())
                }
                val newPage = articles.data?.copy(datas = newArticles)
                articles.copy(data = newPage)
            } else {
                wanService.getArticles(nextPageNumber).throwError()
            }
            LoadResult.Page(
                data = (response.data?.datas ?: emptyList()).toMutableList(),
                prevKey = null,
                nextKey = if (response.data?.over == true) null else response.data?.curPage ?: 0 + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}