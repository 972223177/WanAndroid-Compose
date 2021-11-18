@file:OptIn(ExperimentalPagerApi::class)
package com.ly.wanandroid.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
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
        DisposableEffect(key1 = itemCount, effect = {

            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.targetPage + 1) % pagerState.pageCount)
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
            indicator(pagerState.targetPage % itemCount)

        }
    } else {
        Spacer(modifier = Modifier.defaultMinSize())
    }
}