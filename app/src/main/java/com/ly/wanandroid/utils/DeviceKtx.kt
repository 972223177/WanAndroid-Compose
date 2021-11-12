package com.ly.wanandroid.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import java.util.*

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

private const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID_99599"

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission", "HardwareIds")
fun getImei(): String {
    return try {
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> {
                getUUID()
            }
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED -> {
                val telephonyManager = getSystemService<TelephonyManager>(Context.TELEPHONY_SERVICE)
                val uniqueId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telephonyManager?.imei
                } else {
                    telephonyManager?.deviceId
                }
                uniqueId ?: getUUID()
            }
            else -> {
                getUUID()
            }
        }
    } catch (e: Exception) {
        ""
    }
}


fun getUUID(): String {
    val sharedPrefs = appContext.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
    var uniqueId = sharedPrefs.getString(PREF_UNIQUE_ID, null)
    if (uniqueId == null) {
        uniqueId = UUID.randomUUID().toString()
        sharedPrefs.edit {
            putString(PREF_UNIQUE_ID, uniqueId)
        }
    }
    return uniqueId
}