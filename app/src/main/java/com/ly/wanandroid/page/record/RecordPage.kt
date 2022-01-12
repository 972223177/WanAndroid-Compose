package com.ly.wanandroid.page.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.base.widgets.CommonAppBar
import com.ly.wanandroid.config.RecordManager
import com.ly.wanandroid.config.readArticle
import com.ly.wanandroid.domain.RecordModel
import com.ly.wanandroid.ui.theme.onMainOrSurface
import com.ly.wanandroid.ui.theme.textSecond
import com.ly.wanandroid.ui.theme.textSurface

@Composable
fun RecordPage() {
    val records by RecordManager.recordListFlow.collectAsState()
    val navController = LocalNavController.current
    Scaffold(topBar = {
        CommonAppBar(backPress = { navController.popBackStack() }, title = "阅读历史", actions = {
            Text(
                text = "清除",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onMainOrSurface,
                modifier = Modifier
                    .clickable {
                        RecordManager.clear()
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            )
        })
    }) {
        LazyColumn(content = {
            items(records) { item ->
                RecordItem(navController = navController, recordModel = item)
            }
        })
    }
}

@Composable
private fun RecordItem(navController: NavHostController, recordModel: RecordModel) {
    Column(modifier = Modifier
        .clickable {
            navController.readArticle(recordModel.article)
        }
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = recordModel.article.title,
            fontSize = 15.sp,
            color = MaterialTheme.colors.textSurface
        )
        Text(
            text = recordModel.date,
            fontSize = 13.sp,
            color = MaterialTheme.colors.textSecond,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

