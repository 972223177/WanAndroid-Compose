package com.ly.wanandroid

import android.app.Application
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.utils.*
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InitTaskRunner(this)
            .add(syncInitTask("Utils") {
                Utils.init(it)
            })
            .add(syncInitTask("SpUtils") {
                SpUtils.init(it)
            }).add(asyncInitTask("x5Web") {
                QbSdk.initTbsSettings(
                    mapOf(
                        TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
                        TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true
                    )
                )
                QbSdk.disableSensitiveApi()
                QbSdk.setDownloadWithoutWifi(false)
                QbSdk.initX5Environment(it, object : QbSdk.PreInitCallback {
                    override fun onCoreInitFinished() {
                        logD(InitTaskRunner.TAG, "TBS init finished")
                    }

                    override fun onViewInitFinished(p0: Boolean) {
                        logD(InitTaskRunner.TAG, "TBS init successful:$p0")
                    }

                })
            }).run()
        Setting.init()
    }

    override fun onTerminate() {
        super.onTerminate()
        Utils.close()
    }
}