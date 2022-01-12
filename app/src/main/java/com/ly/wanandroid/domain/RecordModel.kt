package com.ly.wanandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RecordModel(val date: String, val article: Article) : Parcelable

@Parcelize
@Serializable
data class AllRecordModel(val recordModels: List<RecordModel> = emptyList()) : Parcelable {

    companion object {
        val DEFAULT = AllRecordModel(recordModels = emptyList())
    }
}