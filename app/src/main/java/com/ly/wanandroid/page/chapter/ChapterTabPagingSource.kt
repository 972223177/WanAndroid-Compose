package com.ly.wanandroid.page.chapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.usecase.HomeUseCase

class ChapterTabPagingSource(private val useCase: HomeUseCase, private val id: Int) :
    PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPageNumber = params.key ?: 0
            val result = useCase.getKnowledgeArticleList(nextPageNumber, id)
            LoadResult.Page(
                data = result.data?.datas ?: emptyList(),
                prevKey = null,
                nextKey = if (result.data?.over == true) null else result.data?.curPage ?: 0 + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}