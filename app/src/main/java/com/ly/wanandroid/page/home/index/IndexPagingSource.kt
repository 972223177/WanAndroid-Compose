package com.ly.wanandroid.page.home.index

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.usecase.HomeUseCase

class IndexPagingSource(private val useCase: HomeUseCase) :
    PagingSource<Int, Article>() {


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? =
        state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPageNumber = params.key ?: 0
            val canLoadTops = Setting.isShowTopArticle
            val response = if (canLoadTops) {
                useCase.getArticlesWithTop(nextPageNumber)
            } else {
                useCase.getArticles(nextPageNumber)
            }
            LoadResult.Page(
                data = response.data?.datas ?: emptyList(),
                prevKey = null,
                nextKey = if (response.data?.over == true) null else response.data?.curPage ?: 0 + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}