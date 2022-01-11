package com.ly.wanandroid.page.web

import android.graphics.Bitmap
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.base.utils.dp2pxf
import com.ly.wanandroid.ui.theme.*
import com.ly.wanandroid.utils.web.WebInstance
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

@ExperimentalAnimationApi
@Composable
fun WebPage(url: String) {
    val context = LocalContext.current
    val webInstance = remember {
        WebInstance.getInstance(context)
    }
    var webView by remember {
        mutableStateOf<WebView?>(null)
    }
    var progress by remember {
        mutableStateOf(0f)
    }

    DisposableEffect(key1 = Unit, effect = {
        webView = webInstance.obtain()
        onDispose {
            webView?.let {
                webInstance.destroy(it)
            }
        }
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val navController = LocalNavController.current
        if (webView != null) {
            AndroidView(factory = {
                return@AndroidView webView!!.apply {
                    initWebView { newProgress ->
                        progress = newProgress
                    }
                    loadUrl(url)
                }
            })
        }
        FloatingActionButton(
            onClick = {
                if (webView?.canGoBack() == true) {
                    webView?.goBack()
                } else {
                    navController.popBackStack()
                }
            },
            backgroundColor = MaterialTheme.colors.surface,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 15.dp, bottom = 15.dp)
                .size(50.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent, shape = CircleShape)
                    .drawBehind {
                        val paint = Paint().apply {
                            strokeWidth = dp2pxf(5f)
                            color = Blue4282F4
                            style = PaintingStyle.Stroke
                        }
                        val circleRect = Rect(center, size.width / 2)
                        drawIntoCanvas {
                            it.drawArc(circleRect, 0f, progress * 360f, false, paint.apply {
                                color = if (progress == 1f) Color.Transparent else Blue4282F4
                            })
                        }
                    }
            ) {
                Icon(
                    imageVector = Icons.Sharp.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
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