package com.ly.wanandroid.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    @SerialName("curPage")
    val curPage: Int,
    @SerialName("datas")
    val datas: List<T>,
    @SerialName("offset")
    val offset: Int,
    @SerialName("over")
    val over: Boolean,
    @SerialName("pageCount")
    val pageCount: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("total")
    val total: Int
)