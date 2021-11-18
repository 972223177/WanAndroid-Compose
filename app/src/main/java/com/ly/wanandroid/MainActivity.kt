package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.sharp.AccountTree
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Message
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModels<MainViewModel>()
    private val titles = listOf("首页", "问答", "体系", "我的")
    private val icons =
        listOf(
            Icons.Sharp.Home,
            Icons.Sharp.Message,
            Icons.Sharp.AccountTree,
            Icons.Sharp.Person
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isNightMode by Setting.isNightModeFlow.collectAsState()
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(Color.Transparent, !isNightMode)
            WanAndroidTheme(darkTheme = isNightMode) {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val navController = rememberNavController()
                    HomePage()
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }


}

object NavDest {
    const val HOME = "home"
    const val QUESTION = "question"
    const val KNOWLEDGE = "knowledge"
    const val MINE = "mine"
    const val SETTING = "setting"
}
