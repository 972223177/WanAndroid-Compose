package com.ly.wanandroid.page.setting

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ly.wanandroid.*
import com.ly.wanandroid.R
import com.ly.wanandroid.base.utils.formatSize
import com.ly.wanandroid.base.utils.getSize
import com.ly.wanandroid.base.utils.getVersionName
import com.ly.wanandroid.base.utils.tryDelete
import com.ly.wanandroid.base.widgets.CommonAppBar
import com.ly.wanandroid.components.ConfirmDialog
import com.ly.wanandroid.components.rememberConfirmState
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.ui.theme.*

@Composable
fun SettingPage() {
    Scaffold(topBar = {
        val navController = LocalNavController.current
        CommonAppBar(backPress = { navController.popBackStack() }, title = "设置")
    }) {
        Column {
            val autoNightMode by Setting.isAutoNightModeFlow.collectAsState()
            SettingItem(title = "跟随系统暗色模式", checked = autoNightMode, onCheckedChanged = {
                Setting.openAutoNightMode(it)
            })
            val nightMode by Setting.isNightModeFlow.collectAsState()
            SettingItem(title = "暗色模式", checked = nightMode, onCheckedChanged = {
                Setting.openNightMode(it)
            })
            val showTop by Setting.isShowTopArticleFlow.collectAsState()
            SettingItem(
                title = "显示置顶",
                subTitle = "开启后首页显示置顶文章",
                checked = showTop,
                onCheckedChanged = {
                    Setting.openShowTopArticle(it)
                })
            val showBanner by Setting.enableShowBannerFlow.collectAsState()
            SettingItem(
                title = "显示轮播",
                subTitle = "开启后首页顶部显示轮播图",
                checked = showBanner,
                onCheckedChanged = {
                    Setting.openShowBanner(it)
                })
            val saveRecord by Setting.enableSaveBrowserRecordFlow.collectAsState()
            SettingItem(
                title = "保存浏览记录",
                subTitle = "开启后保存文章浏览记录",
                checked = saveRecord,
                onCheckedChanged = {
                    Setting.enableSaveBrowserRecord(it)
                }
            )
            val context = LocalContext.current
            var cacheSize by remember {
                mutableStateOf(context.getCacheSize())
            }
            val confirmState = rememberConfirmState()
            ConfirmDialog(
                state = confirmState,
                content = "是否要清理缓存",
                negativeText = "取消",
                positiveText = "确定"
            ) {
                context.cacheDir.tryDelete()
                cacheSize = context.getCacheSize()
                Setting.refresh()
            }
            SettingItem(title = "清除缓存", subTitle = cacheSize) {
                confirmState.showing = true
            }
            SettingItem(title = "当前版本", subTitle = getVersionName()) {

            }

        }
    }
}


private fun Context.getCacheSize(): String {
    return cacheDir.getSize().formatSize()
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
        Switch(checked = isChecked, colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.switcherThumbChecked,
            checkedTrackColor = MaterialTheme.colors.switcherTrackChecked
        ), onCheckedChange = {
            isChecked = it
            onCheckedChanged(it)
        })
    }
}

@Composable
private fun SettingItem(
    title: String,
    subTitle: String = "",
    clickable: VoidCallback
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            clickable()
        }
        .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 15.sp, color = MaterialTheme.colors.textSurface)
        Row(modifier = Modifier.sizeIn()) {
            if (subTitle.isNotEmpty()) {
                Text(
                    text = subTitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.textThird,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_enter),
                contentDescription = "arrowRight",
                tint = MaterialTheme.colors.iconSecond
            )
        }
    }
}