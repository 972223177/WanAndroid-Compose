package com.ly.wanandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Navi(
    @SerialName("articles")
    val articles: List<Article> = listOf(),
    @SerialName("cid")
    val cid: Int = 0,
    @SerialName("name")
    val name: String = ""
) : Parcelable
