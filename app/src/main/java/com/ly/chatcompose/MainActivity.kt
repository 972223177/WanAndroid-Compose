package com.ly.chatcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ly.chatcompose.components.CScaffold
import com.ly.chatcompose.config.WanService
import com.ly.chatcompose.config.createService
import com.ly.chatcompose.conversation.BackPressHandler
import com.ly.chatcompose.conversation.LocalBackPressedDispatcher
import com.ly.chatcompose.databinding.ContentMainBinding
import com.ly.chatcompose.model.Banner
import com.ly.chatcompose.screen.HomeScreen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModels<MainViewModel>()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                    val scaffoldState = rememberScaffoldState()
                    val drawerOpen by mViewModel.drawerShouldBeOpened.collectAsState()
                    if (drawerOpen) {
                        LaunchedEffect(key1 = Unit, block = {
                            scaffoldState.drawerState.open()
                            mViewModel.resetOpenDrawerAction()
                        })
                    }
                    val scope = rememberCoroutineScope()
                    if (scaffoldState.drawerState.isOpen) {
                        BackPressHandler {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }
                    CScaffold(
                        scaffoldState = scaffoldState,
                        onProfileClicked = {
                            val bundle = bundleOf("userId" to it)
                            findNavController().navigate(R.id.nav_profile, bundle)
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        },
                        onChatClicked = {
                            findNavController().popBackStack(R.id.nav_home, true)
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }) {
//                            AndroidViewBinding(factory = ContentMainBinding::inflate)
                        Surface {
                            HomeScreen()
                        }
                    }
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
