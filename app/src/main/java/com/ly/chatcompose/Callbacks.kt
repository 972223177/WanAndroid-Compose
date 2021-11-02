package com.ly.chatcompose

import androidx.compose.runtime.Composable

typealias VoidCallback = () -> Unit

typealias ValueGetter<T> = () -> T

typealias ValueSetter<T> = (T) -> Unit

typealias ValueCallback<T, R> = (T) -> R

typealias AsyncVoidCallback = suspend () -> Unit

typealias AsyncValueGetter<T> = suspend () -> T

typealias AsyncValueSetter<T> = suspend (T) -> Unit

typealias AsyncValueCallback<T, R> = suspend (T) -> R

typealias ComposableCallback = @Composable () -> Unit

typealias ComposableCallback1<T> = @Composable (T) -> Unit

typealias ComposableCallback2<T, T1> = @Composable (T, T1) -> Unit

typealias ComposableCallback3<T, T1, T2> = @Composable (T, T1, T2) -> Unit

typealias ComposableExtCallback<T> = @Composable T.() -> Unit

typealias ComposableExtCallback1<T, V> = @Composable T.(V) -> Unit

typealias ComposableExtCallback2<T, V, R> = @Composable T.(V, R) -> Unit
