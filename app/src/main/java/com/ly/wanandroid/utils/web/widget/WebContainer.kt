package com.ly.wanandroid.utils.web.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class WebContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = -1
) : FrameLayout(context, attributeSet, defStyle) {

    private val mTouchSlop by lazy(LazyThreadSafetyMode.NONE) {
        ViewConfiguration.get(context).scaledTouchSlop
    }
    private val mTapTimeout by lazy(LazyThreadSafetyMode.NONE) {
        ViewConfiguration.getTapTimeout()
    }
    private val mDoubleTapTimeout by lazy(LazyThreadSafetyMode.NONE) {
        ViewConfiguration.getDoubleTapTimeout()
    }

    private var mDownTime = 0L
    private var mDownX = 0f
    private var mDownY = 0f
    private var mLastDownX = 0f
    private var mLastDownY = 0f
    private var mLastTouchTime = 0L
    private var mOnDoubleClickListener: ((x: Float, y: Float) -> Unit)? = null
    private var mOnTouchDownListener: (() -> Unit)? = null
//    private var mHeartAnimators = LinkedList<Animator>()
    private var mDoubleClicked = false
    private val mDoubleClickTimeoutRunnable = {
        mDoubleClicked = false
    }

    init {
        setBackgroundColor(0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDownX = width / 2f
        mDownY = height / 2f
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = it.x
                    mDownY = it.y
                    mDownTime = System.currentTimeMillis()
                    mOnTouchDownListener?.invoke()
                }
                MotionEvent.ACTION_UP -> {
                    val upX = it.x
                    val upY = it.y
                    val currTime = System.currentTimeMillis()
                    val inTouchSlop = getDistance(mDownX, mDownY, upX, upY) < mTouchSlop
                    val inTabTimeout = (currTime - mDownTime) < mTapTimeout
                    val isClick = inTouchSlop && inTabTimeout
                    if (isClick) {
                        if ((currTime - mLastTouchTime) < mDoubleTapTimeout) {
                            if (getDistance(
                                    mDownX,
                                    mDownY,
                                    mLastDownX,
                                    mLastDownY
                                ) < mTouchSlop * 5
                            ) {
                                if (!mDoubleClicked) {
                                    mDoubleClicked = true
                                    mOnDoubleClickListener?.invoke(upX, upY)
                                }
                            }
                        }
                        mLastDownX = mDownX
                        mLastDownY = mDownY
                        mLastTouchTime = currTime
                    }
//                    if (mDoubleClicked && inTouchSlop) {
//                        onDoubleClicking(upX,upY)
//                    }
                    removeCallbacks(mDoubleClickTimeoutRunnable)
                    postDelayed(mDoubleClickTimeoutRunnable, (mDoubleTapTimeout * 3).toLong())
                }
                else -> {}
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }

//    override fun onDetachedFromWindow() {
//        for (animator in mHeartAnimators) {
//            animator.cancel()
//        }
//        mHeartAnimators.clear()
//        super.onDetachedFromWindow()
//    }

//    private fun onDoubleClicking(x:Float,y: Float){
//        showHearAnim(x, y)
//    }
//
//    fun showHearAnim(x:Float,y: Float){
//
//    }


    private fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(abs(x2 - x1).pow(2.0f) + abs(y2 - y1).pow(2.0f))
    }

    fun setOnDoubleClickListener(block: (Float, Float) -> Unit) {
        mOnDoubleClickListener = block
    }

    fun setOnTouchDownListener(block: () -> Unit) {
        mOnTouchDownListener = block
    }
}