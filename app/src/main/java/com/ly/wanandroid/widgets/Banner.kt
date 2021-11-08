package com.ly.wanandroid.widgets

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ly.wanandroid.ComposableCallback1
import com.ly.wanandroid.ComposableExtCallback1
import com.ly.wanandroid.VoidCallback
import kotlinx.coroutines.launch
import java.util.*

/**
 * 不能使用PageState的currentPage和pageCount
 */
@ExperimentalPagerApi
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    itemCount: Int,
    initialPage: Int = 0,
    pagerState: PagerState = rememberPagerState(initialPage = initialPage),
    duration: Long = 3000L,
    indicator: ComposableExtCallback1<BoxScope, Int> = { },
    whenDispose: VoidCallback = {},
    onItemChanged: ComposableCallback1<Int>
) {
    val scope = rememberCoroutineScope()
    if (itemCount > 0) {
        val nextPage = remember {
            mutableStateOf(initialPage)
        }
        DisposableEffect(key1 = itemCount, effect = {

            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    scope.launch {
                        nextPage.value = (pagerState.currentPage + 1) % itemCount
                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                    }
                }
            }
            timer.schedule(timerTask, duration, duration)
            onDispose {
                whenDispose()
                timer.cancel()
                timerTask.cancel()
            }
        })
        Box(modifier = modifier) {
            HorizontalPager(
                count = Int.MAX_VALUE,
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) { page ->
                val realIndex = page % itemCount
                onItemChanged(realIndex)
            }
            indicator(pagerState.currentPage % itemCount)

        }
    } else {
        Spacer(modifier = Modifier.defaultMinSize())
    }
}