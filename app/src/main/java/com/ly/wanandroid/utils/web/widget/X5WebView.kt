package com.ly.wanandroid.utils.web.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ScrollingView
import com.tencent.smtt.sdk.WebView

class X5WebView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = -1,
    b: Boolean = false,
    map: Map<String, Any>? = null
) : WebView(context, attributeSet, defStyle, map, b), ScrollingView {
    override fun canScrollHorizontally(direction: Int): Boolean {
        return view.canScrollHorizontally(direction)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return view.canScrollVertically(direction)
    }
}