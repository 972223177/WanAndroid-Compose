package com.ly.wanandroid.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.RequiresPermission

/**
 * 到拨号页
 */
fun Context.dialingPhone(phoneNumber: String) {
    if (phoneNumber.isEmpty()) return
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    startActivity(intent)
}

/**
 * 拨打电话
 */
@RequiresPermission(value = "android.permission.CALL_PHONE")
fun Context.callPhone(phoneNumber: String) {
    if (phoneNumber.isEmpty()) return
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
}

/**
 * 到系统设置
 */
fun Context.goToSetting() {
    startActivity(Intent(Settings.ACTION_SETTINGS))
}

/**
 * 浏览器
 */
fun Context.openBrowser(url: String) {
    if (url.isEmpty()) return
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


