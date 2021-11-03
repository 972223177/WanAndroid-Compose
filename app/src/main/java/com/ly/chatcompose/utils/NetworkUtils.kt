package com.ly.chatcompose.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build


fun isNetworkAvailable(): Boolean {
    val capabilities = getNetworkCapabilities() ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

//移动数据网络
fun isCellularNetwork(): Boolean {
    val capabilities = getNetworkCapabilities() ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

fun isWifi(): Boolean {
    val capabilities = getNetworkCapabilities() ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}

@SuppressLint("ObsoleteSdkInt")
fun getConnectivityManager(): ConnectivityManager? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getSystemService()
    } else {
        getSystemService(Context.CONNECTIVITY_SERVICE)
    }
}

fun getNetworkCapabilities(): NetworkCapabilities? {
    val manager = getConnectivityManager() ?: return null
    return manager.getNetworkCapabilities(manager.activeNetwork)
}