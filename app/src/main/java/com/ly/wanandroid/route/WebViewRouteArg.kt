package com.ly.wanandroid.route

import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class WebViewRouteArg(var url: String, val showTitle: Boolean = false) : RouteArg



