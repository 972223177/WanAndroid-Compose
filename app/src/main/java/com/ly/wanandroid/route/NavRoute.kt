package com.ly.wanandroid.route

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavHostController
import com.ly.wanandroid.config.http.globalJson
import com.ly.wanandroid.utils.logD
import kotlinx.serialization.encodeToString

object NavRoute {
    const val HOME = "home"

    const val SETTING = "setting"

    const val WEB_VIEW = "webView"

}

interface RouteArg : Parcelable

inline fun <reified T : RouteArg> T.toJson(): String = globalJson.encodeToString(this)

fun NavHostController.navTo(
    destinationName: String,
    argName: String,
    arg: Parcelable,
    backStackRouteName: String? = null,
    isLaunchSingleTop: Boolean = true,
    needToRestoreState: Boolean = true
) {
    currentBackStackEntry?.arguments?.putParcelable(argName, arg)
    navigate(destinationName){
        if (backStackRouteName != null) {
            popUpTo(backStackRouteName) { saveState = true }
        }
        launchSingleTop = isLaunchSingleTop
        restoreState = needToRestoreState
    }
}

fun NavHostController.navTo(
    destinationName: String,
    arg: Any? = null,
    backStackRouteName: String? = null,
    isLaunchSingleTop: Boolean = true,
    needToRestoreState: Boolean = true
) {
    val routeArg = if (arg != null) {
        when (arg) {
            is RouteArg -> String.format("/%s", Uri.encode(arg.toJson()))
            is String, is Int, is Float, is Double, is Boolean, is Long -> String.format("/%s", arg)
            else -> throw RuntimeException("unSupport type")
        }
    } else {
        ""
    }
    logD("导航:$destinationName$routeArg")
    navigate("$destinationName$routeArg") {
        if (backStackRouteName != null) {
            popUpTo(backStackRouteName) { saveState = true }
        }
        launchSingleTop = isLaunchSingleTop
        restoreState = needToRestoreState
    }
}


fun NavHostController.goWebView(url: String, showTitle: Boolean = false) {
    navTo(NavRoute.WEB_VIEW,"webArg" ,WebViewRouteArg(url, showTitle))
}