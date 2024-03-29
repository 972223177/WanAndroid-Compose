package com.ly.wanandroid.route

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.ly.wanandroid.config.http.globalJson
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString


@Parcelize
@Serializable
data class WebRouteArg(var url: String) : Parcelable

class WebRouteArgType : NavType<WebRouteArg>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): WebRouteArg? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): WebRouteArg {
        return globalJson.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: WebRouteArg) {
        bundle.putParcelable(key, value)
    }
}



