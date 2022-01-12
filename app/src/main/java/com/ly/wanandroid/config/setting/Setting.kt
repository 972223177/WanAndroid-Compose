package com.ly.wanandroid.config.setting

import com.ly.wanandroid.base.utils.isSystemNightModeOpened
import com.ly.wanandroid.base.utils.preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Setting {
    private const val SAVE_ID = "Setting"

    /**
     * 是否是夜间模式
     */
    var isNightMode by preference(false, saveId = SAVE_ID)
        private set
    private val mIsNightModeFlow = MutableStateFlow(isNightMode)
    val isNightModeFlow: StateFlow<Boolean> = mIsNightModeFlow

    /**
     * 夜间模式跟随系统
     */
    var isAutoNightMode by preference(false, saveId = SAVE_ID)
        private set

    private val mIsAutoNightModeFlow = MutableStateFlow(isAutoNightMode)
    val isAutoNightModeFlow: StateFlow<Boolean> = mIsAutoNightModeFlow

    /**
     * 是否显示置顶文章
     */
    var isShowTopArticle by preference(true, saveId = SAVE_ID)
        private set
    private val mIsShowTopArticleFlow = MutableStateFlow(isShowTopArticle)
    val isShowTopArticleFlow: StateFlow<Boolean> = mIsShowTopArticleFlow


    /**
     * 是否保存浏览记录
     */
    var enableSaveBrowserRecord by preference(true, saveId = SAVE_ID)
        private set
    private val mEnableSaveBrowserRecordFlow = MutableStateFlow(enableSaveBrowserRecord)
    val enableSaveBrowserRecordFlow: StateFlow<Boolean> = mEnableSaveBrowserRecordFlow

    /**
     * 首页是否显示头部Banner
     */
    var enableShowBanner by preference(true, saveId = SAVE_ID)
        private set
    private val mEnableShowBannerFlow = MutableStateFlow(enableShowBanner)
    val enableShowBannerFlow: StateFlow<Boolean> = mEnableShowBannerFlow

    fun init() {
        if (isAutoNightMode) {
            isNightMode = isSystemNightModeOpened()
            mIsNightModeFlow.value = isNightMode
        }
    }

    fun openShowBanner(show: Boolean) {
        enableShowBanner = show
        mEnableShowBannerFlow.value = show
    }

    fun openNightMode(open: Boolean) {
        if (!isAutoNightMode) {
            isNightMode = open
            mIsNightModeFlow.value = isNightMode
        }
    }

    fun openAutoNightMode(open: Boolean) {
        isAutoNightMode = open
        mIsAutoNightModeFlow.value = isAutoNightMode
        if (isAutoNightMode) {
            isNightMode = isSystemNightModeOpened()
            mIsNightModeFlow.value = isNightMode
        }
    }

    fun openShowTopArticle(open: Boolean) {
        isShowTopArticle = open
        mIsShowTopArticleFlow.value = isShowTopArticle
    }

    fun enableSaveBrowserRecord(enable: Boolean) {
        enableSaveBrowserRecord = enable
        mEnableSaveBrowserRecordFlow.value = enableSaveBrowserRecord
    }

    fun refresh() {
        mEnableShowBannerFlow.value = enableShowBanner
        mEnableSaveBrowserRecordFlow.value = enableSaveBrowserRecord
        mIsAutoNightModeFlow.value = isAutoNightMode
        mIsNightModeFlow.value = isNightMode
        mIsShowTopArticleFlow.value = isShowTopArticle
    }
}