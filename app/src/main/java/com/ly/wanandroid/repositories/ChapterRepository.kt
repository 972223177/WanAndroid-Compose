package com.ly.wanandroid.repositories

import com.ly.wanandroid.config.http.HttpResult
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.domain.Chapter
import javax.inject.Inject


class ChapterRepository @Inject constructor() {
    suspend fun getChapters(): HttpResult<List<Chapter>> = wanService.tree()
}