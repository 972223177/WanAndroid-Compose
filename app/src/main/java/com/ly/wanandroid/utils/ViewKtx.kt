package com.ly.wanandroid.utils

import android.os.SystemClock
import android.view.View
import android.view.ViewTreeObserver


inline fun View.clickDebounce(interval: Long = 600L, crossinline block: (View) -> Unit) {
    var timestamp = 0L
    setOnClickListener {
        val now = SystemClock.elapsedRealtime()
        val plus = now - timestamp
        if (plus > interval) {
            timestamp = now
            block(it)
        }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.onPreDraw(crossinline block: View.() -> Unit) =
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnPreDrawListener(this)
            }
            block()
            return true
        }
    })



