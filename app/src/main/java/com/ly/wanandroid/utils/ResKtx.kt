package com.ly.wanandroid.utils

import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat


fun drawableRes(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(appContext, id)

fun stringRes(@StringRes id: Int): String = appContext.getString(id)

fun colorRes(@ColorRes id: Int): Int = ContextCompat.getColor(appContext, id)

fun stringArrayRes(@ArrayRes id: Int): Array<String> = appContext.resources.getStringArray(id)

fun dimensRes(@DimenRes id:Int):Float = appContext.resources.getDimension(id)



