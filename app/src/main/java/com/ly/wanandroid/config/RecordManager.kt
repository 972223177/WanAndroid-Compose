package com.ly.wanandroid.config


import androidx.navigation.NavHostController
import com.ly.wanandroid.base.utils.launchAppScope
import com.ly.wanandroid.base.utils.preference
import com.ly.wanandroid.config.http.globalJson
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.domain.AllRecordModel
import com.ly.wanandroid.domain.Article
import com.ly.wanandroid.domain.RecordModel
import com.ly.wanandroid.route.goWebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.text.SimpleDateFormat
import java.util.*

object RecordManager {
    private val mDateFormat by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINESE)
    }

    private const val SAVE_ID = "RecordManager"

    private var allRecordModelJson by preference("", saveId = SAVE_ID)

    private val mRecordListFlow = MutableStateFlow<List<RecordModel>>(emptyList())

    val recordListFlow: StateFlow<List<RecordModel>> = mRecordListFlow

    private val mutex = Mutex()

    init {
        launchAppScope(Dispatchers.Default) {
            mutex.withLock {
                mRecordListFlow.value = jsonDecode(allRecordModelJson).recordModels
            }
        }
    }

    fun save(article: Article) {
        if (Setting.enableSaveBrowserRecord) {
            launchAppScope(Dispatchers.Default) {
                mutex.withLock {
                    val date = mDateFormat.format(Date(System.currentTimeMillis()))
                    val model = RecordModel(date, article)
                    val oldAllModel = jsonDecode(allRecordModelJson)
                    val list = oldAllModel.recordModels.toMutableList()
                    list.removeAll {
                        it.article.id == article.id
                    }
                    list.add(model)
                    allRecordModelJson = jsonEncode(oldAllModel.copy(recordModels = list))
                    mRecordListFlow.value = list
                }
            }
        }
    }

    fun clear() {
        allRecordModelJson = ""
        mRecordListFlow.value = emptyList()
    }

    private fun jsonDecode(json: String): AllRecordModel {
        return globalJson.runCatching {
            decodeFromString<AllRecordModel>(json)
        }.getOrDefault(AllRecordModel.DEFAULT)
    }

    private fun jsonEncode(allRecordModel: AllRecordModel): String {
        return globalJson.runCatching {
            encodeToString(allRecordModel)
        }.getOrDefault("")
    }
}

fun NavHostController.readArticle(article: Article) {
    RecordManager.save(article)
    goWebView(article.link)
}