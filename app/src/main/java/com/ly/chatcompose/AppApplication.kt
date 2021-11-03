package com.ly.chatcompose

import android.app.Application
import com.ly.chatcompose.utils.SpUtils
import com.ly.chatcompose.utils.Utils
import com.tencent.mmkv.MMKV

class AppApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        SpUtils.init(this)
    }
}