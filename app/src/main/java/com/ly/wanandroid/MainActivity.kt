package com.ly.wanandroid

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ly.wanandroid.components.CScaffold
import com.ly.wanandroid.conversation.BackPressHandler
import com.ly.wanandroid.conversation.LocalBackPressedDispatcher
import com.ly.wanandroid.utils.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModels<MainViewModel>()

    @ExperimentalPermissionsApi
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
//                            HomeScreen()
                            var mosaic: Bitmap? = null
                            val writePermission =
                                rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {

                                AndroidView(factory = {
                                    val view = AppCompatEditText(it)
                                    view.layoutParams = ViewGroup.LayoutParams(dp2px(150f),WRAP_CONTENT)
                                    view.divideWithGroup(4,'-')
                                    return@AndroidView view
                                }, modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable {
                                        this@MainActivity.goToSetting()
                                    })
                            }
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
