package com.ly.wanandroid.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    @SerialName("curPage")
    val curPage: Int = 0,
    @SerialName("datas")
    val datas: List<T> = listOf(),
    @SerialName("offset")
    val offset: Int = 0,
    @SerialName("over")
    val over: Boolean = false,
    @SerialName("pageCount")
    val pageCount: Int = 0,
    @SerialName("size")
    val size: Int = 0,
    @SerialName("total")
    val total: Int = 0
)