package com.ly.chatcompose.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.ColorInt
import java.io.File

/**
 * 保存到相册
 * todo android10 以下还有问题
 */
fun Bitmap.saveGallery(picName: String): Boolean {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, picName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(dir, picName)
            put(MediaStore.MediaColumns.DATA, file.path)
        }
    }
    val insertUri = appContext.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: return false

    return appContext.contentResolver.openOutputStream(insertUri)?.use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
        true
    } ?: false
}

/**
 * 合成水印
 * @param mark 水印
 * @param x 坐标
 * @param y 坐标
 */
fun Bitmap.mark(mark: Bitmap, x: Float, y: Float): Bitmap {
    val result = copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(result)
    val paint = Paint()
    canvas.drawBitmap(this, 0f, 0f, paint)
    canvas.drawBitmap(mark, x, y, paint)
    return result
}

/**
 * 文字水印
 * @param text 文字
 * @param textSize 文字大小 像素单位
 * @param textColor 文字颜色
 * @param alpha 透明度
 */
fun Bitmap.mark(
    text: String,
    textSize: Float,
    @ColorInt textColor: Int,
    alpha: Int,
    xOffsetPercent: Float,
    yOffsetPercent: Float,
    inside: Boolean
): Bitmap {
    val result = copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(result)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    canvas.drawBitmap(this, 0f, 0f, paint)
    paint.textSize = textSize
    paint.color = textColor
    paint.alpha = alpha
    val rect = Rect()
    paint.getTextBounds(text, 0, text.length, rect)
    val x: Float
    val y: Float
    if (inside) {
        x = (width - rect.right) * xOffsetPercent
        y = (height - rect.bottom) * yOffsetPercent
    } else {
        x = width * xOffsetPercent
        y = height * yOffsetPercent
    }
    canvas.drawText(text, x, y, paint)
    return result
}

/**
 * 马赛克
 *
 * @param zoneSize 方块大小
 * @param startX   马赛克区域起点x
 * @param startY   马赛克区域起点y
 * @param endX     马赛克区域宽
 * @param endY     马赛克区域高
 */
fun Bitmap.mosaic(zoneSize: Int, startX: Int, startY: Int, endX: Int, endY: Int): Bitmap {
    val w: Int = width
    val h: Int = height
    val x1 = if (startX < 0) 0 else if (startX > w) w else startX
    val y1 = if (startY < 0) 0 else if (startY > h) h else startY
    val x2 = if (endX < 0) 0 else if (endX > w) w else endX
    val y2 = if (endY < 0) 0 else if (endY > h) h else endY
    val l = if (x1 < x2) x1 else x2
    val t = if (y1 < y2) y1 else y2
    val r = if (x1 < x2) x2 else x1
    val b = if (y1 < y2) y2 else y1
    val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val c = Canvas(result)
    val p = Paint()
    var i1 = l
    var i2: Int
    while (i1 < r) {
        var j1 = t
        var j2: Int
        while (j1 < b) {
            i2 = if (i1 + zoneSize > r) r else i1 + zoneSize
            j2 = if (j1 + zoneSize > b) b else j1 + zoneSize
            p.color = getPixel((i1 + i2) / 2, (j1 + j2) / 2)
            c.drawRect(i1.toFloat(), j1.toFloat(), i2.toFloat(), j2.toFloat(), p)
            j1 += zoneSize
        }
        i1 += zoneSize
    }
    return result
}

/**
 * 全图马赛克
 */
fun Bitmap.mosaic(zoneWidth: Int): Bitmap = mosaic(zoneWidth, 0, 0, width, height)

/**
 * @param zoneCount 像素方块在短边的个数
 * @return Bitmap
 */
fun Bitmap.mosaicWithCount(zoneCount: Int): Bitmap {
    val min = width.coerceAtMost(height)
    if (zoneCount >= min) return this
    return mosaic(min / zoneCount)
}
