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
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.ly.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.launch

@ExperimentalPagerApi
class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModels<MainViewModel>()
    private val titles = listOf("首页", "问答", "体系", "我的")
    private val icons =
        listOf(
            Icons.Rounded.Home,
            Icons.Rounded.QuestionAnswer,
            Icons.Rounded.AccountTree,
            Icons.Rounded.Person
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    @Composable
    private fun HomePage() {
        val homeNavController = rememberNavController()
        val pageState = rememberPagerState()
        Scaffold(
            bottomBar = {
                BottomNav(pageState)
            },
        ) {
            HorizontalPager(
                count = titles.size,
                state = pageState,
                modifier = Modifier.padding(it)
            ) { page ->
                Screen(route = titles[page], index = page)
            }
        }
    }

    @Composable
    private fun BottomNav(pagerState: PagerState) {
        val scope = rememberCoroutineScope()
        BottomNavigation(
            modifier = Modifier.navigationBarsPadding(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            icons.forEachIndexed { index, imageVector ->
                val route = titles[index]
                BottomNavigationItem(selected = pagerState.currentPage == index, icon = {
                    Icon(imageVector = imageVector, contentDescription = imageVector.name)
                }, label = {
                    Text(text = route)
                }, onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
            }
        }
    }

    @Composable
    private fun Screen(route: String, index: Int) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var counter by remember {
                mutableStateOf(0)
            }
            Text(text = "$route $counter", modifier = Modifier.align(Alignment.Center))
            Button(onClick = {
                counter += 1
            }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(text = "add")
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
