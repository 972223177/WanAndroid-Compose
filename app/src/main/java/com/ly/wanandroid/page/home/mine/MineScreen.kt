package com.ly.wanandroid.page.home.mine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.utils.isSystemNightModeOpened

@Composable
fun MineScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            var darkModel by remember {
                mutableStateOf(Setting.isNightMode)
            }
            Text(text = if (darkModel) "关闭" else "打开", modifier = Modifier.clickable {
                darkModel = !darkModel
                Setting.openNightMode(darkModel)
            }.align(Alignment.Center))
        }
    }
}