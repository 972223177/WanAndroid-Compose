package com.ly.chatcompose.utils

import android.os.SystemClock
import android.view.View


inline fun View.clickDebounce(interval: Long = 600L,crossinline block: (View) -> Unit) {
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

