package com.ly.wanandroid

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.ConcurrentHashMap

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val cache = ConcurrentHashMap<Int, Int>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Test
    fun addition_isCorrect() = runBlocking {
        cache[1] = 112
        cache[3] = 324
        cache[8] = 523
        for (i in 0..10) {
            var value: Int? = cache[i]
            println("getInCache key:$i,value:$value")
            if (value == null) {
                value = withContext(Dispatchers.IO) {
                    getRandomValue()
                }
                cache[i] = value
                println("getRandomValue key:$i,value:$value")
            } else {
                coroutineScope.launch(Dispatchers.IO) {
                    delay(100L)
                    val newValue = getRandomValue()
                    println("createNewValue in scope key:$i,value:$newValue")
                    cache[i] = newValue
                }
            }
        }
        coroutineScope.launch(Dispatchers.Default) {
            for (i in 0..10) {
                val newValue = withContext(Dispatchers.IO) {
                    getRandomValue()
                }
                cache[i] = newValue
                println("create key:$i,value:$newValue")
            }
        }
        println("wait 1s")
        delay(1000L)

        coroutineScope.launch(Dispatchers.Default) {
            for (i in 0..10) {
                val value = cache[i]
                println("end2 key:$i,value:$value")
            }
        }
        delay(5000L)

    }

    private val mRandom = java.util.Random()
    private fun getRandomValue(): Int = mRandom.nextInt(100)
}
