package com.ly.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ly.wanandroid.base.widgets.BasePage
import com.ly.wanandroid.page.chapter.ChapterPage
import com.ly.wanandroid.page.chapter.ChapterViewModel
import com.ly.wanandroid.page.home.HomePage
import com.ly.wanandroid.page.login.LoginPage
import com.ly.wanandroid.page.record.RecordPage
import com.ly.wanandroid.page.setting.SettingPage
import com.ly.wanandroid.page.web.WebPage
import com.ly.wanandroid.route.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberAnimatedNavController()
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = NavRoute.HOME,
                    enterTransition = {
                        slideInHorizontally {
                            it
                        }
                    },
                    exitTransition = {
                        slideOutHorizontally {
                            -it
                        }
                    },
                    popEnterTransition = {
                        slideInHorizontally {
                            -it
                        }
                    },
                    popExitTransition = {
                        slideOutHorizontally {
                            it
                        }
                    }) {
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
                    createPage(navController, NavRoute.LOGIN) {
                        LoginPage()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
inline fun NavGraphBuilder.createPage(
    navHostController: NavHostController,
    route: String,
    darkIcons: Boolean = false,
    noinline enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    noinline exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    noinline popEnterTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?
    )? = enterTransition,
    noinline popExitTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?
    )? = exitTransition,
    crossinline content: ComposableCallback
) {
    composable(
        route = route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
    ) {
        BasePage(navController = navHostController, darkIcons = darkIcons) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
inline fun <reified T> NavGraphBuilder.createArgPage(
    navHostController: NavHostController,
    route: String,
    argKey: String,
    routeType: NavType<*>,
    darkIcons: Boolean = false,
    noinline enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    noinline exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    noinline popEnterTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?
    )? = enterTransition,
    noinline popExitTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?
    )? = exitTransition,
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
            }),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
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
