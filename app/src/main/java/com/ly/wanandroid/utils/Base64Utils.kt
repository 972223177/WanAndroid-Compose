package com.ly.wanandroid.utils

import android.util.Base64
import java.nio.charset.StandardCharsets

object Base64Utils {
    fun encodeToBytes(source: ByteArray?): ByteArray {
        if (source == null || source.isEmpty()) return ByteArray(0)
        return Base64.encode(source, Base64.NO_WRAP)
    }

    fun encodeToBytes(source: String?): ByteArray {
        return encodeToBytes(source?.toByteArray(StandardCharsets.UTF_8))
    }

    fun encodeToString(source: ByteArray?): String {
        val bytes = encodeToBytes(source)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun encodeToString(source: String?): String {
        val bytes = encodeToBytes(source)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun decodeToBytes(source: ByteArray?): ByteArray {
        if (source == null || source.isEmpty()) return ByteArray(0)
        return Base64.decode(source, Base64.NO_WRAP)
    }

    fun decodeToBytes(source: String?): ByteArray {
        return decodeToBytes(source?.toByteArray(StandardCharsets.UTF_8))
    }

    fun decodeToString(source: ByteArray?): String {
        val bytes = decodeToBytes(source)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun decodeToString(source: String?): String {
        val bytes = decodeToBytes(source)
        return String(bytes, StandardCharsets.UTF_8)
    }
}

