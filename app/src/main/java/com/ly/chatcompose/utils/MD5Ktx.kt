package com.ly.chatcompose.utils

import okhttp3.internal.and
import java.io.File
import java.io.FileInputStream
import java.lang.StringBuilder
import java.security.MessageDigest


fun String.md5Encode(): String {
    if (isEmpty()) return ""
    val catchResult = runCatching {
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(this.toByteArray())
        val result = StringBuilder()
        for (b in bytes) {
            val temp = Integer.toHexString(b and 0xff)
            result.append(if (temp.length == 1) "0$temp" else temp)
        }
        result.toString()
    }
    catchResult.exceptionOrNull()?.printStackTrace()
    return catchResult.getOrNull() ?: ""
}

fun String.md5Encode(times: Int = 0): String {
    if (isEmpty()) return ""
    if (times <= 1) return md5Encode()
    var md5 = md5Encode()
    for (i in 0..times) {
        md5 = md5.md5Encode()
    }
    return md5
}

fun String.md5Encode(slat: String): String = "$this$slat".md5Encode()

fun File.md5Encode(): String {
    if (!exists() || !isFile) return ""
    val catchResult = runCatching {
        val result = StringBuilder()
        val buffer = ByteArray(8192)
        var len: Int
        val md5 = MessageDigest.getInstance("MD5")
        FileInputStream(this).use { fileInputStream ->
            while ((fileInputStream.read(buffer).also { len = it }) != -1) {
                md5.update(buffer, 0, len)
            }
            val bytes = md5.digest()
            for (byte in bytes) {
                val temp = Integer.toHexString(byte and 0xff)
                result.append(if (temp.length == 1) "0$temp" else temp)
            }
            result.toString()
        }
    }
    catchResult.exceptionOrNull()?.printStackTrace()
    return catchResult.getOrNull() ?: ""
}