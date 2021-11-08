package com.ly.wanandroid.utils

import android.util.Log
import com.ly.wanandroid.BuildConfig


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
