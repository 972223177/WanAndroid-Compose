package com.ly.wanandroid.page.home

import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(currentPage = 0)
}