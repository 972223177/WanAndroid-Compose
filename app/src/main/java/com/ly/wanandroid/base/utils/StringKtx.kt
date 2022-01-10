package com.ly.wanandroid.base.utils


fun String.removeAllBlank(): String {
    if (isNullOrEmpty()) return this
    return Regex("\\s*|\t|\r|\n").replace(this, "")
}