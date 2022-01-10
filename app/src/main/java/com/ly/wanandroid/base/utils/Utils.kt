package com.ly.wanandroid.base.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("StaticFieldLeak")
object Utils {

    private var context: Context? = null

    fun init(context: Context) {
        if (context !is Application) throw RuntimeException("必须在application中初始化")
        Utils.context = context
    }

    fun getAppContext(): Context {
        if (context == null) {
            throw RuntimeException("未在Application中初始化")
        }
        return context!!
    }

    /**
     * invoke this when application onTerminate
     */
    fun close() {
        mCoroutineMap.values.forEach {
            if (it is Closeable) {
                try {
                    it.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}

val appContext: Context
    get() = Utils.getAppContext()

val packageName: String
    get() = appContext.packageName

private const val JOB_KEY = "com.ly.appScope.JOB_KEY"

private val mCoroutineMap = mutableMapOf<String, CoroutineScope>()

@Suppress("SameParameterValue")
private fun getTag(key: String): CoroutineScope? = synchronized(mCoroutineMap) {
    mCoroutineMap[key]
}

@Suppress("SameParameterValue")
private fun setTagIfAbsent(key: String, scope: CoroutineScope): CoroutineScope {
    var previous: CoroutineScope?
    synchronized(mCoroutineMap) {
        previous = mCoroutineMap[key]
        if (previous == null) {
            mCoroutineMap[key] = scope
        }
    }
    return previous ?: scope
}

private class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}

fun launchAppScope(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val scope = getTag(JOB_KEY)
    return (scope
        ?: setTagIfAbsent(
            JOB_KEY,
            CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )).launch(context, start, block)
}

@RequiresApi(Build.VERSION_CODES.M)
inline fun <reified T> getSystemService(): T? = appContext.getSystemService(T::class.java)

@Suppress("UNCHECKED_CAST")
fun <T> getSystemService(name: String): T? = appContext.getSystemService(name) as? T

fun toast(msg: String) {
    if (msg.isEmpty()) return
    launchAppScope {
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
    }
}