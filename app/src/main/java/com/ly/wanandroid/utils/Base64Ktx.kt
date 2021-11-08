package com.ly.wanandroid.utils

import android.util.Base64
import java.nio.charset.StandardCharsets

fun ByteArray.encodeBase64(): ByteArray {
    if (isEmpty()) return ByteArray(0)
    return Base64.encode(this, Base64.NO_WRAP)
}

fun String.encodeBase64(): ByteArray {
    if (isEmpty()) return ByteArray(0)
    return toByteArray(StandardCharsets.UTF_8).encodeBase64()
}