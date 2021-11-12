package com.ly.wanandroid

import android.app.Application
import com.ly.wanandroid.utils.syncInitTask
import com.ly.wanandroid.utils.InitTaskRunner
import com.ly.wanandroid.utils.SpUtils
import com.ly.wanandroid.utils.Utils

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InitTaskRunner(this)
            .add(syncInitTask("Utils") {
                Utils.init(it)
            })
            .add(syncInitTask("SpUtils") {
                SpUtils.init(it)
            }).run()
    }

    override fun onTerminate() {
        super.onTerminate()
        Utils.close()
    }
}