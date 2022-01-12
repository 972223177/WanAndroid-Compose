package com.ly.wanandroid.route

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.ly.wanandroid.config.http.globalJson
import com.ly.wanandroid.domain.Chapter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

@Parcelize
@Serializable
data class ChapterRouteArg(val targetIndex: Int, val chapter: Chapter) : Parcelable

class ChapterRouteType : NavType<ChapterRouteArg>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ChapterRouteArg? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): ChapterRouteArg {
        return globalJson.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: ChapterRouteArg) {
        bundle.putParcelable(key, value)
    }

}