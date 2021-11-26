package com.ly.wanandroid.route

import android.net.Uri
import com.ly.wanandroid.config.http.globalJson
import com.ly.wanandroid.ui.RouteArg
import com.ly.wanandroid.utils.Base64Utils
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.net.URLDecoder
import java.net.URLEncoder


@Parcelize
@Serializable
data class WebViewRouteArg(var url: String, val showTitle: Boolean = false) : RouteArg {
    fun getArg(): String = Uri.encode(
        globalJson.encodeToString(
            WebViewRouteArg(
                url = URLEncoder.encode(url, "utf-8"), showTitle = showTitle
            )
        )
    )

    companion object {
        fun parseArg(arg: String): WebViewRouteArg =
            globalJson.decodeFromString<WebViewRouteArg>(Uri.decode(arg)).also {
                it.url = URLDecoder.decode(it.url, "utf-8")
            }
    }

}



