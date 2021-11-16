package com.ly.wanandroid.utils

import android.os.CountDownTimer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


inline fun CoroutineScope.countdown(
    totalMillis: Long,
    intervalMillis: Long,
    crossinline onFinish: (() -> Unit) = {},
    crossinline onTick: (Long) -> Unit
): Job {
    return launch {
        flow {
            for (i in totalMillis downTo 0 step intervalMillis) {
                delay(intervalMillis)
                if (this@launch.isActive) {
                    val millis = if (i - intervalMillis <= 0) 0L else i
                    emit(millis)
                } else {
                    break
                }
            }
        }.onCompletion {
            onFinish()
        }.distinctUntilChanged()
            .onEach { onTick(it) }
            .collect()
    }
}

inline fun countdown(
    totalMillis: Long,
    intervalMillis: Long,
    crossinline onFinish: (() -> Unit) = {},
    crossinline onTick: (Long) -> Unit
): CountDownTimer {
    return object : CountDownTimer(totalMillis, intervalMillis) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            onFinish()
        }

    }.also {
        it.start()
    }
}