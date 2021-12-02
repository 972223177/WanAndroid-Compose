package com.ly.wanandroid.page.web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.ly.wanandroid.utils.web.WebInstance

@Composable
fun WebPage(url: String, showTitle: Boolean = false) {
    Surface {
        Box(modifier = Modifier.fillMaxHeight()) {
            AndroidView(factory = {
                return@AndroidView WebInstance.getInstance(it).obtain().apply {
                    loadUrl(url)
                }
            })
        }
    }
}