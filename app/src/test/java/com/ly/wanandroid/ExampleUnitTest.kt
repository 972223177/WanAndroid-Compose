package com.ly.wanandroid

import android.os.CountDownTimer
import com.ly.wanandroid.utils.countdown
import kotlinx.coroutines.*
import okhttp3.internal.toHexString
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() = runBlocking {
        var lastMillis = System.currentTimeMillis()
        val job = countdown(10 * 1000L, 1000L, onFinish = {
            val currentMillis = System.currentTimeMillis()
            println("${currentMillis - lastMillis}->onFinish")
        }) {
            val currentMillis = System.currentTimeMillis()
            println("${currentMillis - lastMillis}->$it")
            lastMillis = currentMillis
        }

        launch {
            delay(2000L)
            job.cancel()
        }
        Unit
    }
}
