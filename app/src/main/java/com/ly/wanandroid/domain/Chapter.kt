package com.ly.wanandroid.domain


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Chapter(
    @SerialName("children")
    val children: List<Chapter> = listOf(),
    @SerialName("courseId")
    val courseId: Int = 0,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("order")
    val order: Int = 0,
    @SerialName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerialName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerialName("visible")
    val visible: Int = 0
) : Parcelable