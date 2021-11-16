package com.ly.wanandroid.utils

import android.content.res.Configuration
import android.os.Build

@Suppress("DEPRECATION")
fun getVersionCode(): Long {
    return try {
        appContext.run {
            packageManager.getPackageInfo(packageName, 0).let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    it.longVersionCode
                } else {
                    it.versionCode.toLong()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

fun getVersionName(): String {
    return try {
        appContext.run {
            packageManager.getPackageInfo(packageName, 0).versionName
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 注意高版本需要在manifest.xml中的设query属性
 */
fun isAppInstalled(packageName: String): Boolean {
    return try {
        appContext.packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 系统是否打开了夜间模式
 */
fun isSystemNightModeOpened(): Boolean {
    val uiMode = appContext.resources.configuration.uiMode
    return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}