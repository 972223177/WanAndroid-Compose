package com.ly.wanandroid.config.setting

import com.ly.wanandroid.utils.SpUtils
import com.ly.wanandroid.utils.isSystemNightModeOpened
import com.ly.wanandroid.utils.preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Setting {
    private const val SAVE_ID = "Setting"

    /**
     * 是否是夜间模式
     */
    var isNightMode by preference(false, saveId = SAVE_ID)
        private set
    private val _isNightModeFlow = MutableStateFlow(isNightMode)
    val isNightModeFlow: StateFlow<Boolean> = _isNightModeFlow

    /**
     * 夜间模式跟随系统
     */
    var isAutoNightMode by preference(false, saveId = SAVE_ID)
        private set

    private val _isAutoNightModeFlow = MutableStateFlow(isAutoNightMode)
    val isAutoNightModeFlow: StateFlow<Boolean> = _isAutoNightModeFlow

    /**
     * 是否显示置顶文章
     */
    var isShowTopArticle by preference(true, saveId = SAVE_ID)
        private set
    private val _isShowTopArticleFlow = MutableStateFlow(isShowTopArticle)
    val isShowTopArticleFlow: StateFlow<Boolean> = _isShowTopArticleFlow


    /**
     * 是否保存浏览记录
     */
    var enableSaveBrowserRecord by preference(true, saveId = SAVE_ID)
        private set
    private val _enableSaveBrowserRecordFlow = MutableStateFlow(enableSaveBrowserRecord)
    val enableSaveBrowserRecordFlow: StateFlow<Boolean> = _enableSaveBrowserRecordFlow

    fun init() {
        if (isAutoNightMode) {
            isNightMode = isSystemNightModeOpened()
            _isNightModeFlow.value = isNightMode
        }
    }

    fun clearBrowserRecord() {
        SpUtils.remove("enableSaveBrowserRecord", SAVE_ID)
    }


    fun openNightMode(open: Boolean) {
        if (!isAutoNightMode) {
            isNightMode = open
            _isNightModeFlow.value = isNightMode
        }
    }

    fun openAutoNightMode(open: Boolean) {
        isAutoNightMode = open
        _isAutoNightModeFlow.value = isAutoNightMode
        if (isAutoNightMode) {
            isNightMode = isSystemNightModeOpened()
            _isNightModeFlow.value = isNightMode
        }
    }

    fun openShowTopArticle(open: Boolean) {
        isShowTopArticle = open
        _isShowTopArticleFlow.value = isShowTopArticle
    }

    fun enableSaveBrowserRecord(enable: Boolean) {
        enableSaveBrowserRecord = enable
        _enableSaveBrowserRecordFlow.value = enableSaveBrowserRecord
    }

}