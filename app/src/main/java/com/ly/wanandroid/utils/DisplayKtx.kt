package com.ly.wanandroid.utils

import android.util.DisplayMetrics

/**
 * dp 转 px
 */
fun dp2px(dpValue: Float): Int {
    val scale = getDensity()
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun px2dp(pxValue: Float): Int {
    val scale = getDensity()
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun sp2px(spValue: Float): Int {
    val scale = getScaleDensity()
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun px2sp(pxValue: Float): Int {
    val scale = getScaleDensity()
    return (pxValue / scale + 0.5f).toInt()
}

fun getDisplayMetrics(): DisplayMetrics = appContext.resources.displayMetrics

fun getDensityDpi(): Int = getDisplayMetrics().densityDpi

fun getDensity(): Float = getDisplayMetrics().density

fun getScaleDensity(): Float = getDisplayMetrics().scaledDensity

fun getXDpi(): Float = getDisplayMetrics().xdpi

fun getYDpi(): Float = getDisplayMetrics().ydpi