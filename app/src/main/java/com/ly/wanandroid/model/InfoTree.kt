package com.ly.wanandroid.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoTree(
    @SerialName("children")
    val children: List<InfoTree>,
    @SerialName("courseId")
    val courseId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int,
    @SerialName("parentChapterId")
    val parentChapterId: Int,
    @SerialName("visible")
    val visible: Int
)