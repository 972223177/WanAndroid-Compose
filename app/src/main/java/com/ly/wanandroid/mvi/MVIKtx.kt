package com.ly.wanandroid.mvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlin.reflect.KProperty1


inline fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    crossinline block: (A) -> Unit
) {
    map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged()
        .observe(lifecycleOwner) { (a) ->
            block(a)
        }
}

inline fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    crossinline block: (A, B) -> Unit
) {
    map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged()
        .observe(lifecycleOwner) { (a, b) ->
            block(a, b)
        }
}

inline fun <T, A, B, C> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    crossinline block: (A, B, C) -> Unit
) {
    map {
        StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
    }.distinctUntilChanged()
        .observe(lifecycleOwner) { (a, b, c) ->
            block(a, b, c)
        }
}

inline fun <T> MutableLiveData<T>.setState(reducer: T.() -> T) {
    value = value?.reducer()
}

fun <T> SingleLiveEvent<T>.setEvent(value: T) {
    setValue(value)
}

inline fun <T> LiveData<T>.withState(block: (T) -> Unit) {
    value?.let(block)
}

data class StateTuple1<A>(val a: A)

data class StateTuple2<A, B>(val a: A, val b: B)

data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)