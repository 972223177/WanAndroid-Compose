package com.ly.wanandroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Parcelize
@Serializable
data class LoginData(
    @SerialName("admin")
    val admin: Boolean = false,
    @SerialName("chapterTops")
    val chapterTops: List<String> = listOf(),
    @SerialName("coinCount")
    val coinCount: Int = 0,
    @SerialName("collectIds")
    val collectIds: List<Int> = listOf(),
    @SerialName("email")
    val email: String = "",
    @SerialName("icon")
    val icon: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("password")
    val password: String = "",
    @SerialName("publicName")
    val publicName: String = "",
    @SerialName("token")
    val token: String = "",
    @SerialName("type")
    val type: Int = 0,
    @SerialName("username")
    val username: String = ""
) : Parcelable {

    companion object {
        val DEFAULT = LoginData()
    }
}