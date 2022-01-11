package com.ly.wanandroid.route

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavHostController
import com.ly.wanandroid.base.utils.logD
import com.ly.wanandroid.config.http.globalJson
import com.ly.wanandroid.domain.Chapter
import kotlinx.serialization.encodeToString

object NavRoute {
    const val HOME = "home"

    const val SETTING = "setting"

    const val WEB_VIEW = "webView"

    const val CHAPTER = "chapter"
}

object NavArgKey {
    const val WEB = "webArg"

    const val CHAPTER = "chapterArg"
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
            is String, is Int, is Float, is Double, is Boolean, is Long -> String.format("/%s", arg)
            else -> throw RuntimeException("unSupport type")
        }
    } else {
        ""
    }
//    logD("导航:$destinationName$routeArg")
    navigate("$destinationName$routeArg") {
        if (backStackRouteName != null) {
            popUpTo(backStackRouteName) { saveState = true }
        }
        launchSingleTop = isLaunchSingleTop
        restoreState = needToRestoreState
    }
}

inline fun <reified T : Parcelable> T.toJson(): String = Uri.encode(globalJson.encodeToString(this))

fun NavHostController.goWebView(url: String) {
    navTo(NavRoute.WEB_VIEW, WebRouteArg(url).toJson())
}

fun NavHostController.goChapter(initialIndex: Int, chapter: Chapter) {
    navTo(NavRoute.CHAPTER, ChapterRouteArg(initialIndex, chapter).toJson())
}

fun NavHostController.goSetting() {
    navTo(NavRoute.SETTING)
}