package com.ly.wanandroid.page.web

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.ui.theme.main
import com.ly.wanandroid.utils.web.WebInstance
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

@ExperimentalAnimationApi
@Composable
fun WebPage(url: String, showTitle: Boolean = false) {
    Surface {
        var progress by remember {
            mutableStateOf(0f)
        }

        Scaffold(topBar = {
            TopAppBar {

            }
        }) {
            Box(modifier = Modifier.padding(it)) {
                AndroidView(factory = { context ->
                    return@AndroidView WebInstance.getInstance(context).obtain().apply {
                        initWebView { newProgress ->
                            progress = newProgress
                        }
                        loadUrl(url)
                    }
                })
                AnimatedVisibility(visible = progress < 0.99f) {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.main
                    )
                }
            }
        }
    }
}

private inline fun WebView.initWebView(crossinline progressCallback: ValueSetter<Float>) {
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, progress: Int) {
            super.onProgressChanged(view, progress)
            progressCallback(progress / 100f)
        }
    }
    webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressCallback(0f)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressCallback(1.0f)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }
    }
}