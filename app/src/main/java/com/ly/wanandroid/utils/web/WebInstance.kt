package com.ly.wanandroid.utils.web

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.ly.wanandroid.utils.web.widget.X5WebView
import com.tencent.smtt.export.external.interfaces.IX5WebSettings
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import java.lang.Exception

class WebInstance private constructor(private val application: Application) {

    private val mCache = mutableListOf<WebView>()

    init {
        mCache.add(create())
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Suppress("DEPRECATION")
    fun obtain(): WebView {
        if (mCache.isEmpty()) return create()
        return mCache.removeAt(0).also {
            it.settings.javaScriptEnabled = true
            it.clearHistory()
            it.resumeTimers()
        }
    }

    @Suppress("DEPRECATION")
    fun recycle(webView: WebView) {
        (webView.parent as? ViewGroup)?.removeView(webView)
        try {
            webView.stopLoading()
            var step = 0
            while (webView.canGoBackOrForward(step - 1)) {
                step--
            }
            webView.goBackOrForward(step)
            webView.loadDataWithBaseURL(null, ",", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.settings.javaScriptEnabled = false
            webView.pauseTimers()
            webView.webChromeClient = null
            webView.webViewClient = null
        } catch (e: Exception) {

        } finally {
            if (!mCache.contains(webView)) {
                mCache.add(webView)
            }
        }
    }

    fun destroy(webView: WebView) {
        recycle(webView)
        try {
            webView.removeAllViews()
            webView.destroy()
        } catch (e: Exception) {
        } finally {
            mCache.remove(webView)
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    fun create(): WebView {
        return X5WebView(application).apply {
            background = ColorDrawable(Color.TRANSPARENT)
            setBackgroundColor(0)
            background.alpha = 0
            overScrollMode = WebView.OVER_SCROLL_NEVER
            view.overScrollMode = View.OVER_SCROLL_NEVER
            with(settings) {
                javaScriptEnabled = true
                //设置自适应屏幕
                useWideViewPort = true // 将图片调整到合适的大小
                loadWithOverviewMode = true//缩放至屏幕大小
                setVerticalScrollbarOverlay(false)
                setHorizontalScrollbarOverlay(false)
                javaScriptCanOpenWindowsAutomatically = false//是否允许通过js打开新窗口
                loadsImagesAutomatically = true //是否自动加载图片
                defaultTextEncodingName = "UTF-8"//编码格式
                allowFileAccess = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
                builtInZoomControls = false //是否禁止缩放
                displayZoomControls = false //是否隐藏原生的缩放控件
                setAppCacheEnabled(true)
                domStorageEnabled = true
                setGeolocationEnabled(true)
                setAppCacheMaxSize(Long.MAX_VALUE)
                pluginState = WebSettings.PluginState.ON_DEMAND
                cacheMode = WebSettings.LOAD_DEFAULT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                settingsExtension?.also {
                    it.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        CookieManager.getInstance().setAcceptThirdPartyCookies(this@apply, true)
                    }
                }
            }
        }

    }

    companion object {
        private var mInstance: WebInstance? = null

        fun getInstance(context: Context): WebInstance {
            if (mInstance == null) {
                mInstance = WebInstance(context.applicationContext as Application)
            }
            return mInstance!!
        }
    }
}