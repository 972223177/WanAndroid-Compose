package com.ly.wanandroid.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@SuppressLint("StaticFieldLeak")
object Utils {

    private var context: Context? = null

    fun init(context: Context) {
        if (context !is Application) throw RuntimeException("必须在application中初始化")
        this.context = context
    }

    fun getAppContext(): Context {
        if (context == null) {
            throw RuntimeException("未在Application中初始化")
        }
        return context!!
    }

}

val appContext: Context
    get() = Utils.getAppContext()

@RequiresApi(Build.VERSION_CODES.M)
inline fun <reified T> getSystemService(): T? = appContext.getSystemService(T::class.java)

@Suppress("UNCHECKED_CAST")
fun <T> getSystemService(name: String): T? = appContext.getSystemService(name) as? T