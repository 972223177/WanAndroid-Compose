package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.page.web.WebPage
import com.ly.wanandroid.route.NavArgKey
import com.ly.wanandroid.route.NavRoute
import com.ly.wanandroid.route.WebRouteArg
import com.ly.wanandroid.route.WebRouteArgType
import com.ly.wanandroid.base.utils.logD
import com.ly.wanandroid.base.widgets.BasePage
import dagger.hilt.android.AndroidEntryPoint

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
                        BasePage(navController = navController) {
                            HomePage()
                        }
                    }
                    composable(
                        route = NavRoute.WEB_VIEW + "/{${NavArgKey.WEB}}",
                        arguments = listOf(navArgument(NavArgKey.WEB) {
                            type = WebRouteArgType()
                        })
                    ) {
                        BasePage(navController = navController, true) {
                            val arg = it.arguments?.getParcelable<WebRouteArg>(NavArgKey.WEB)
                            if (arg != null) {
                                WebPage(url = arg.url)
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
