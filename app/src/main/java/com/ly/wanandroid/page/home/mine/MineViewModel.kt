package com.ly.wanandroid.page.home.mine

import androidx.lifecycle.ViewModel
import com.ly.wanandroid.config.setting.User
import com.ly.wanandroid.model.LoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor() : ViewModel() {
    val user: StateFlow<LoginData> = User.loginDataFlow
}