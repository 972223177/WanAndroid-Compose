package com.ly.wanandroid.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

fun CharSequence.copyText() {
    ClipboardUtil.copyText(this)
}

fun CharSequence.copyText(label: CharSequence?) {
    ClipboardUtil.copyText(label, this)
}

fun LifecycleOwner.addClipboardChangedListener(block: () -> Unit) {
    val observer = ClipboardLifecycleObserver(lifecycle, block)
    observer.register()
}


private class ClipboardLifecycleObserver(
    private val lifecycle: Lifecycle,
    private val block: () -> Unit
) :
    LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            ClipboardUtil.removeChangedListener(block)
        }
    }

    fun register() {
        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) return
        lifecycle.addObserver(this)
        ClipboardUtil.addChangedListener(block)
    }

}

object ClipboardUtil {

    /**
     * 清理长按时的黏贴文本内容，不是剪贴板的内容
     */
    fun clearPrimaryClip() {
        copyText(null, "")
    }

    fun copyText(label: CharSequence?, text: CharSequence) {
        getService()?.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun copyText(text: CharSequence) {
        copyText(packageName, text)
    }

    fun getPrimaryLabel(): CharSequence {
        return getService()?.primaryClipDescription?.label ?: ""
    }

    fun getPrimaryText(): CharSequence {
        val clip = getService()?.primaryClip ?: return ""
        if (clip.itemCount > 0) {
            return clip.getItemAt(0).coerceToText(appContext) ?: ""
        }
        return ""
    }

    fun addChangedListener(block: () -> Unit) {
        getService()?.addPrimaryClipChangedListener(block)
    }

    fun removeChangedListener(block: () -> Unit) {
        getService()?.removePrimaryClipChangedListener(block)
    }

    private fun getService(): ClipboardManager? = getSystemService(Context.CLIPBOARD_SERVICE)
}




