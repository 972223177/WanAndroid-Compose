package com.ly.wanandroid

import android.app.Application
import com.ly.wanandroid.utils.SpUtils
import com.ly.wanandroid.utils.Utils
import com.tencent.mmkv.MMKV

class AppApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        SpUtils.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Utils.close()
    }
}