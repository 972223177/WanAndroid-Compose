package com.ly.wanandroid.config.setting

import com.ly.wanandroid.domain.LoginData
import com.ly.wanandroid.base.utils.preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object User {
    const val CACHE_USER = "cacheUser"
    var loginData by preference(LoginData.DEFAULT, key = CACHE_USER)
        private set

    private val mLoginDataFlow = MutableStateFlow(loginData)
    val loginDataFlow: StateFlow<LoginData> = mLoginDataFlow

    fun login(loginData: LoginData) {
        update(loginData)
    }

    fun update(loginData: LoginData) {
        this.loginData = loginData
        mLoginDataFlow.value = loginData
        if (!isLogin()) {
            logout()
        }
    }

    fun logout() {
        this.loginData = LoginData.DEFAULT
        mLoginDataFlow.value = LoginData.DEFAULT
    }

    fun isLogin(): Boolean {
        if (loginData == LoginData.DEFAULT) return false
        if (loginData.id <= 0) return false
        return true
    }

    fun id() = loginData.id


}