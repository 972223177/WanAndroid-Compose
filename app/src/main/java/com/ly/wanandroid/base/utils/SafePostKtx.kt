package com.ly.wanandroid.base.utils

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

/**
 * 代替view.post的形式，具体原理可以看协程源码关于Android中Dispatcher.Main的具体实现HandlerContext，断点时注意当前Coroutine版本
 * 在ComponentActivity中直接 safePost{ }
 * 在Fragment中：
 *              1.safePost{ }
 *              2.viewLifecycleOwner.safePost{ } 使用getViewLifecycleOwner时如果不能保证生命周期安全
 *              最好try-catch，此方法在viewLifecycleOwner为空时会抛异常
 * @param postWhen post事件的具体时机
 * @param block 你的runnable的具体实现
 */
inline fun LifecycleOwner.safePost(
    postWhen: Lifecycle.State? = null,
    crossinline block: () -> Unit
) {
    postWhen.runBlock(this, 0L, block)
}

/**
 * 同上，多个delay
 */
inline fun LifecycleOwner.safePostDelay(
    millis: Long,
    postWhen: Lifecycle.State? = null,
    crossinline block: () -> Unit
) {
    postWhen.runBlock(this, millis, block)
}

inline fun Lifecycle.State?.runBlock(
    lifecycleOwner: LifecycleOwner,
    millis: Long,
    crossinline block: () -> Unit
) {
    with(lifecycleOwner) {
        when (this@runBlock) {
            Lifecycle.State.DESTROYED -> {
            }
            Lifecycle.State.INITIALIZED -> {
            }
            Lifecycle.State.CREATED -> lifecycleScope.launchWhenCreated {
                launchOnMain(millis, block)
            }
            Lifecycle.State.STARTED -> lifecycleScope.launchWhenStarted {
                launchOnMain(
                    millis,
                    block
                )
            }
            Lifecycle.State.RESUMED -> lifecycleScope.launchWhenResumed {
                launchOnMain(
                    millis,
                    block
                )
            }
            else -> lifecycleScope.launch { launchOnMain(millis, block) }
        }
    }
}

inline fun CoroutineScope.launchOnMain(millis: Long = 0L, crossinline block: () -> Unit) {
    launch(Dispatchers.Main) {
        if (millis > 0L) {
            delay(millis)
        }
        if (isActive) {
            block()
        }
    }
}

/**
 * 自动移除callback，需要手动传lifecycleOwner
 */
fun View?.safePost(lifecycleOwner: LifecycleOwner, block: () -> Unit) {
    if (this == null) return
    val postImpl = ViewSafePostImpl(lifecycleOwner.lifecycle, this, 0L, block)
    lifecycleOwner.lifecycle.addObserver(postImpl)
    postImpl.register()
}

fun View?.safePostDelay(lifecycleOwner: LifecycleOwner, millis: Long, block: () -> Unit) {
    if (this == null) return
    val postImpl = ViewSafePostImpl(lifecycleOwner.lifecycle, this, millis, block)
    lifecycleOwner.lifecycle.addObserver(postImpl)
    postImpl.register()
}

class ViewSafePostImpl(
    private val lifecycle: Lifecycle,
    private val view: View,
    private val millis: Long = 0L,
    private val block: () -> Unit
) : LifecycleEventObserver {

    fun register() {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
        with(view) {
            if (millis > 0L) {
                postDelayed(block, millis)
            } else {
                post(block)
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            view.removeCallbacks(block)
        }
    }

}