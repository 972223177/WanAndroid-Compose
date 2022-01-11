package com.ly.wanandroid.page.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.base.widgets.CommonAppBar
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.ui.theme.*

@Composable
fun SettingPage() {
    Column {
        val navController = LocalNavController.current
        CommonAppBar(backPress = { navController.popBackStack() }, title = "设置")
        SettingItem(title = "跟随系统暗色模式", checked = Setting.isAutoNightMode, onCheckedChanged = {
            Setting.openAutoNightMode(it)
        })
    }
}

@Composable
private fun SettingItem(
    title: String,
    subTitle: String = "",
    checked: Boolean,
    onCheckedChanged: ValueSetter<Boolean>
) {
    var isChecked by remember {
        mutableStateOf(checked)
    }
    Row(modifier = Modifier
        .clickable {
            isChecked = !isChecked
        }
        .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(text = title, fontSize = 15.sp, color = MaterialTheme.colors.textSurface)
            if (subTitle.isNotEmpty()) {
                Text(text = subTitle, fontSize = 12.sp, color = MaterialTheme.colors.textThird)
            }
        }
        Switch(checked = isChecked, onCheckedChange = {
            isChecked = it
            onCheckedChanged(it)
        })
    }
}