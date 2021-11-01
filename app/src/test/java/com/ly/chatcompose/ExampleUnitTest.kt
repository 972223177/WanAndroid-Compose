package com.ly.chatcompose

import com.ly.chatcompose.config.WanService
import com.ly.chatcompose.config.createService
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() = runBlocking {
        val service = createService<WanService>()
        val json = service.searchByAuthor("鸿洋")
        println(json)
    }
}