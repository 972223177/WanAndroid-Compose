package com.ly.wanandroid.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CoinInfo(
    @SerialName("coinCount")
    val coinCount: Int = 0,
    @SerialName("level")
    val level: Int = 0,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("rank")
    val rank: String = "",
    @SerialName("userId")
    val userId: Int = 0,
    @SerialName("username")
    val username: String = ""
)