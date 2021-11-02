package com.ly.chatcompose.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ly.chatcompose.ValueSetter
import com.ly.chatcompose.config.WanService
import com.ly.chatcompose.config.createService
import com.ly.chatcompose.model.Banner
import com.ly.chatcompose.widgets.Banner


@ExperimentalPagerApi
@Composable
fun HomeScreen() {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    val scrollState = rememberScrollState()
    SwipeRefresh(state = refreshState, onRefresh = {

    }) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            HomeBanner(refresh = refreshState.isRefreshing, onItemClick = {

            })

        }
    }
}

@ExperimentalPagerApi
@Composable
private fun HomeBanner(refresh: Boolean, onItemClick: ValueSetter<String>) {
    val banners = remember {
        mutableStateListOf<Banner>()
    }
    LaunchedEffect(key1 = refresh, block = {
        val service = createService<WanService>()
        val result = service.getBanner()
        banners.addAll(result.data ?: emptyList())
    })
    Banner(
        itemCount = banners.size,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        indicator = { index ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(25.dp)
                    .background(shape = CircleShape, color = Color.White)
            ) {
                Text(
                    text = "${index + 1}/${banners.size}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    ) {
        val banner = banners[it]
        Image(
            painter = rememberImagePainter(data = banner.imagePath, builder = {
                crossfade(true)
            }),
            contentDescription = banner.desc,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onItemClick(banner.url)
                }
        )
    }
}