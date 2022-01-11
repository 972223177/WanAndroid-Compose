package com.ly.wanandroid.repositories

import com.ly.wanandroid.config.http.HttpResult
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.domain.Banner
import javax.inject.Inject


class CommonRepository @Inject constructor() {
    suspend fun getBanners(): HttpResult<List<Banner>> = wanService.getBanner()
}