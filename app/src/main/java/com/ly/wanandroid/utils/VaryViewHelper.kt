package com.ly.wanandroid.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

/**
 * view的多状态显示工具
 */
class VaryViewHelper(private var contentView: View) {
    private var parentView: ViewGroup? = null
    private var viewIndex: Int = -1
    private var layoutParam: ViewGroup.LayoutParams? = null
    var currentView: View? = null
        private set

    init {
        initParams()
    }

    private fun initParams() {
        layoutParam = contentView.layoutParams
        parentView = (contentView.parent as? ViewGroup)
            ?: contentView.rootView.findViewById<FrameLayout>(android.R.id.content)
        parentView?.let { parent ->
            val count = parent.childCount
            for (i in 0..count) {
                if (contentView == parent.getChildAt(i)) {
                    viewIndex = i
                    break
                }
            }
        }
        currentView = contentView
    }

    fun showView(view: View) {
        if (parentView == null) {
            initParams()
        }
        currentView = view
        if (parentView?.getChildAt(viewIndex) != view) {
            (view.parent as? ViewGroup)?.removeView(view)
            parentView?.removeViewAt(viewIndex)
            parentView?.addView(view, viewIndex, layoutParam)
        }
    }

    fun showView(@LayoutRes layoutId: Int) {
        val view = LayoutInflater.from(contentView.context).inflate(layoutId, null)
        showView(view)
    }

    fun restore() {
        showView(contentView)
    }
}