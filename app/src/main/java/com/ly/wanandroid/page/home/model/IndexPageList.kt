package com.ly.wanandroid.page.home.model

import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Banner

data class IndexPageList(val banners: List<Banner>, val articles: List<Article>)

data class ArticleWrapper(val anchorPage: Int, val article: Article)
