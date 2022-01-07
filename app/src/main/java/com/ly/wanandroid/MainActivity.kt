package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.config.setting.User
import com.ly.wanandroid.model.LoginData
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.page.web.WebPage
import com.ly.wanandroid.route.NavArgKey
import com.ly.wanandroid.route.NavRoute
import com.ly.wanandroid.route.WebRouteArg
import com.ly.wanandroid.route.WebRouteArgType
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import com.ly.wanandroid.utils.logD
import com.ly.wanandroid.utils.preference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                LaunchedEffect(key1 = Unit) {
                    navController.currentBackStackEntryFlow.collect {
                        logD("currentBackStackEntry:$it")
                    }
                }
                NavHost(navController = navController, startDestination = NavRoute.HOME) {
                    composable(NavRoute.HOME) {
                        CompositionLocalProvider(LocalNavController provides navController) {
                            HomePage()
                        }
                    }
                    composable(
                        route = NavRoute.WEB_VIEW + "/{${NavArgKey.WEB}}",
                        arguments = listOf(navArgument(NavArgKey.WEB) {
                            type = WebRouteArgType()
                        })
                    ) {
                        CompositionLocalProvider(LocalNavController provides navController) {
                            val arg = it.arguments?.getParcelable<WebRouteArg>(NavArgKey.WEB)
                            if (arg != null) {
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
