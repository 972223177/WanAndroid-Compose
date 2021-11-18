package com.ly.wanandroid.utils.web.widget

import android.net.Uri
import com.ly.wanandroid.utils.logD
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class WebChromeClientImpl : WebChromeClient() {
    private var mOnReceivedTitle: ((String) -> Unit)? = null

    private var mOnProgressChanged: ((Int) -> Unit)? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        mOnReceivedTitle?.invoke(title ?: "")
    }

    override fun onProgressChanged(view: WebView?, progress: Int) {
        super.onProgressChanged(view, progress)
        mOnProgressChanged?.invoke(progress)
    }


    fun setOnReceivedTitle(block: (String) -> Unit) {
        mOnReceivedTitle = block
    }

    fun setOnProgressChanged(block: (Int) -> Unit) {
        mOnProgressChanged = block
    }

}


class WebViewClientImpl : WebViewClient() {
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse {

        return super.shouldInterceptRequest(view, url)
    }


    private fun shouldInterceptRequest(
        reqUri: Uri,
        reqHeaders: Map<String, String>? = null,
        reqMethod: String? = null
    ): WebResourceResponse {
        val url = reqUri.toString()
        log("shouldInterceptRequest:url =$url,headers:$reqHeaders,method:$reqMethod")

    }

    private fun log(content: String) {
        logD("WebViewClientImpl", content)
    }
}