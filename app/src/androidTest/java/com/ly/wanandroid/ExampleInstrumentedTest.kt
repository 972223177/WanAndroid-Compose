package com.ly.wanandroid

import android.os.CountDownTimer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ly.wanandroid.utils.Base64Utils
import com.ly.wanandroid.utils.logD
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        var lastMillis = System.currentTimeMillis()
        val job = countdownK(10 * 1000L, 1000L, onFinish = {
            val currentMillis = System.currentTimeMillis()
            println("${currentMillis - lastMillis}->onFinish")
        }) {
            val currentMillis = System.currentTimeMillis()
            println("${currentMillis - lastMillis}->$it")
            lastMillis = currentMillis
        }

//        launch {
//            delay(2000L)
//            job.cancel()
//        }
    }

    inline fun countdownK(
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
}