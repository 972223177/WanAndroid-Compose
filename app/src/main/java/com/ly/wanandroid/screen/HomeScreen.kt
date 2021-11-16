package com.ly.wanandroid.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.config.http.wanService
import com.ly.wanandroid.model.Banner
import com.ly.wanandroid.utils.isNetworkAvailable
import com.ly.wanandroid.widgets.Banner


@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeScreen() {
    Log.d("HomeScreen","network:${isNetworkAvailable()}")
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()
    val useDartIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(
            Color.Transparent,
            darkIcons = useDartIcons
        )
    }
    SwipeRefresh(state = refreshState, onRefresh = {

    }) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            HomeBanner(refresh = refreshState.isRefreshing, onItemClick = {

            })

        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
private fun HomeBanner(refresh: Boolean, onItemClick: ValueSetter<String>) {
    val banners = remember {
        mutableStateListOf<Banner>()
    }
    LaunchedEffect(key1 = refresh, block = {
        val result = wanService.getBanner()
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
        BannerItem(banner = banner, onItemClick = onItemClick)
    }
}

@ExperimentalCoilApi
@Composable
private fun BannerItem(banner: Banner, onItemClick: ValueSetter<String>) {
    val painter = rememberImagePainter(data = banner.imagePath, builder = {
        crossfade(true)
    })
    val state = painter.state

    Image(
        painter = painter,
        contentDescription = banner.desc,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onItemClick(banner.url)
            }
            .placeholder(
                state is ImagePainter.State.Loading,
                color = Color.Gray.copy(alpha = 0.5f)
            )

    )
}