package com.ly.chatcompose.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


/**
 * 基础配置
 * manifest.xml
 * <provider
 *    android:name="androidx.core.content.FileProvider"
 *    android:authorities="${applicationId}.fileprovider"
 *    android:exported="false"
 *    android:grantUriPermissions="true">
 *      <meta-data
 *          android:name="android.support.FILE_PROVIDER_PATHS"
 *          android:resource="@xml/file_paths" />
 * </provider>
 *
 * xml-> file_paths.xml
 *  <?xml version="1.0" encoding="utf-8"?>
 *  <paths>
 *
 *      <root-path
 *          name="root"
 *          path="" />
 *
 *      <files-path
 *          name="files"
 *          path="." />
 *
 *      <cache-path
 *          name="cache"
 *          path="." />
 *
 *      <external-path
 *          name="external"
 *          path="." />
 *
 *      <external-files-path
 *          name="external_file_path"
 *          path="." />
 *
 *      <external-cache-path
 *          name="external_cache_path"
 *          path="." />
 *
 *  </paths>
 **/

fun Context.getUriForFile(file: File): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(this, this.packageName + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
}

fun Context.setIntentData(intent: Intent, file: File, writeAble: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.data = getUriForFile(file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (writeAble) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    } else {
        intent.data = Uri.fromFile(file)
    }
}

fun Context.setIntentDataAndType(
    intent: Intent,
    type: String,
    file: File,
    writeAble: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.setDataAndType(getUriForFile(file), type)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (writeAble) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    } else {
        intent.setDataAndType(Uri.fromFile(file), type)
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.grantPermissions(intent: Intent, uri: Uri, writeAble: Boolean) {
    var flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    if (writeAble) {
        flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
    intent.addFlags(flag)
    val resInfoList =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        grantUriPermission(packageName, uri, flag)
    }
}

