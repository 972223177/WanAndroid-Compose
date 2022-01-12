package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.ly.wanandroid.base.widgets.BasePage
import com.ly.wanandroid.page.chapter.ChapterPage
import com.ly.wanandroid.page.chapter.ChapterViewModel
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.page.record.RecordPage
import com.ly.wanandroid.page.setting.SettingPage
import com.ly.wanandroid.page.web.WebPage
import com.ly.wanandroid.route.*
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
                NavHost(navController = navController, startDestination = NavRoute.HOME) {
                    createPage(navController, NavRoute.HOME) {
                        HomePage()
                    }
                    createArgPage<WebRouteArg>(
                        navController,
                        NavRoute.WEB_VIEW,
                        NavArgKey.WEB,
                        WebRouteArgType(),
                        darkIcons = true
                    ) {
                        WebPage(url = it?.url ?: "")
                    }

                    createArgPage<ChapterRouteArg>(
                        navController, NavRoute.CHAPTER, NavArgKey.CHAPTER, ChapterRouteType()
                    ) {
                        val viewModel: ChapterViewModel = hiltViewModel()
                        viewModel.setChapter(it)
                        ChapterPage(viewModel)
                    }
                    createPage(navController, NavRoute.SETTING) {
                        SettingPage()
                    }
                    createPage(navController, NavRoute.RECORD) {
                        RecordPage()
                    }
                }
            }
        }
    }
}

inline fun NavGraphBuilder.createPage(
    navHostController: NavHostController,
    route: String,
    darkIcons: Boolean = false,
    crossinline content: ComposableCallback
) {
    composable(route = route) {
        BasePage(navController = navHostController, darkIcons = darkIcons) {
            content()
        }
    }
}

inline fun <reified T> NavGraphBuilder.createArgPage(
    navHostController: NavHostController,
    route: String,
    argKey: String,
    routeType: NavType<*>,
    darkIcons: Boolean = false,
    crossinline content: ComposableCallback1<T?>
) {
    if (argKey.isEmpty()) throw IllegalArgumentException("argKey is empty")
    composable(
        route = route.appendArg(argKey),
        arguments = listOf(
            navArgument(
                argKey
            ) {
                type = routeType
            })
    ) {
        BasePage(navController = navHostController, darkIcons = darkIcons) {
            val arg = it.arguments?.get(argKey) as? T
            content(arg)
        }
    }
}

fun String.appendArg(name: String): String = "$this/{$name}"

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
