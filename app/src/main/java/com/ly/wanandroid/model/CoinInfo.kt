package com.ly.wanandroid.model
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


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