package com.ly.wanandroid.base.utils

import android.util.Log
import com.ly.wanandroid.BuildConfig

private const val COMMON_TAG = "WanAndroid"

fun logD(content: String) {
    logD(COMMON_TAG, content)
}

fun logD(tag: String, content: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, content)
    }
}

fun logI(tag: String, content: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, content)
    }
}

fun logE(tag: String, content: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, content)
    }
}

fun logW(tag: String, content: String) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, content)
    }
}

fun logV(tag: String, content: String) {
    if (BuildConfig.DEBUG) {
        Log.v(tag, content)
    }
}
