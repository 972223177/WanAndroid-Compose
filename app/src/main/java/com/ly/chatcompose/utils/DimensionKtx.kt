package com.ly.chatcompose.utils

import android.content.res.Resources

/**
 * dp 转 px
 */
fun dp2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun px2dp(pxValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun sp2px(spValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun px2sp(pxValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}