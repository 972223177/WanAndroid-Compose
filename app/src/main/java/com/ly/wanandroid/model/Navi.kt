package com.ly.wanandroid.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Navi(
    @SerialName("articles")
    val articles: List<Article> = listOf(),
    @SerialName("cid")
    val cid: Int = 0,
    @SerialName("name")
    val name: String = ""
)
