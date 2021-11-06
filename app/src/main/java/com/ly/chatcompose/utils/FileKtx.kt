package com.ly.chatcompose.utils

import android.os.Environment
import java.io.File
import java.lang.Exception
import java.math.BigDecimal

/**
 * sd卡是否可用
 */
fun isSDCardAlive(): Boolean =
    Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)

/**
 * 删除文件或者整个文件夹
 */
fun File.tryDelete(): Boolean {
    if (!exists()) return false
    if (isDirectory) {
        //文件夹的情况，先删除该目录下的文件或文件夹
        list()?.forEach { name ->
            val success = File(this, name).tryDelete()
            if (!success) return false
        }
    }
    return delete()
}

/**
 * 获取文件的实际大小，如果有子文件夹就会往下一层遍历
 */
fun File.getSize(): Long {
    if (!exists()) return 0L
    if (!isDirectory) this.length()
    var size = 0L
    try {
        val files = listFiles()
        if (files.isNullOrEmpty()) return size
        for (file in files) {
            size += if (file.isDirectory) {
                file.getSize()
            } else {
                file.length()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return size
}

/**
 * 需要是字节长度
 * 数值转具体的文件大小附上单位
 */
fun Number.formatSize(): String {
    if (this is Byte || this is Short) return ""
    val kiloByte = this.toDouble() / 1024
    if (kiloByte < 1) return "0KB"
    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        return kiloByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
    }
    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        return megaByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }
    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        return gigaByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }
    return teraBytes.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
}

