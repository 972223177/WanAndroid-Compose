package com.ly.chatcompose.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/***
 * 震动
 */
@Suppress("DEPRECATION")
fun vibrate(mills: Long = 300L) {
    val vibrator = getSystemService<Vibrator>(Context.VIBRATOR_SERVICE) ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(mills, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(mills)
    }
}