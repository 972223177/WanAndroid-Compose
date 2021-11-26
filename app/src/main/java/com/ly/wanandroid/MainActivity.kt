package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.page.web.WebPage
import com.ly.wanandroid.route.WebViewRouteArg
import com.ly.wanandroid.ui.NavRoute
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import com.ly.wanandroid.ui.toRouteArg
import kotlinx.coroutines.flow.collect
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isNightMode by Setting.isNightModeFlow.collectAsState()
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()
            systemUiController.setStatusBarColor(Color.Transparent, !isNightMode)
            WanAndroidTheme(darkTheme = isNightMode) {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    NavHost(navController = navController, startDestination = NavRoute.HOME) {
                        composable(NavRoute.HOME) {
                            CompositionLocalProvider(LocalNavController provides navController) {
                                HomePage()
                            }
                        }
                        composable(NavRoute.WEB_VIEW + "/{url}", arguments = listOf(
                            navArgument("url") { type = NavType.StringType }
                        )) {
                            CompositionLocalProvider(LocalNavController provides navController) {
                                val arg =
                                    WebViewRouteArg.parseArg(it.arguments?.getString("url") ?: "")
                                WebPage(
                                    url = arg.url,
                                    arg.showTitle
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

object LocalNavController {
    private val LocalNavController = compositionLocalOf<NavHostController?> {
        null
    }

    val current: NavHostController
        @Composable
        get() = LocalNavController.current ?: rememberNavController()

    infix fun provides(navHostController: NavHostController): ProvidedValue<NavHostController?> {
        return LocalNavController.provides(navHostController)
    }
}
