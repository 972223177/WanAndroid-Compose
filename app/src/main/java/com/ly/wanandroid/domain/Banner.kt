package com.ly.wanandroid.domain


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    @SerialName("desc")
    val desc: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("imagePath")
    val imagePath: String = "",
    @SerialName("isVisible")
    val isVisible: Int = 0,
    @SerialName("order")
    val order: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("type")
    val type: Int = 0,
    @SerialName("url")
    val url: String = ""
)