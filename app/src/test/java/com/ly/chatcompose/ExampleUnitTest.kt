package com.ly.chatcompose

import com.ly.chatcompose.config.WanService
import com.ly.chatcompose.config.createService
import com.ly.chatcompose.utils.removeAllBlank
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
        val testStr = "1\t\t\n\n2\r3 \n    d"
        println(testStr)
        println(testStr.removeAllBlank())
    }
}