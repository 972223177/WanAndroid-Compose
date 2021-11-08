package com.ly.wanandroid.utils


fun String.removeAllBlank(): String {
    if (isNullOrEmpty()) return this
    return Regex("\\s*|\t|\r|\n").replace(this, "")
}