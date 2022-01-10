package com.ly.wanandroid.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

@SuppressLint("QueryPermissionsNeeded")
fun Context.shareBitmap(bitmap: Bitmap, title: String = "分享到") {
    val file = bitmap.save2Cache() ?: return
    bitmap.recycle()
    val intent = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, getUriForFile(file))
    }, title)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.shareText(url: String, title: String = "分享到") {
    val intent = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }, title)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

