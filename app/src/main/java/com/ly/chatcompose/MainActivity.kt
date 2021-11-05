package com.ly.chatcompose

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ly.chatcompose.components.CScaffold
import com.ly.chatcompose.conversation.BackPressHandler
import com.ly.chatcompose.conversation.LocalBackPressedDispatcher
import com.ly.chatcompose.utils.dp2px
import com.ly.chatcompose.utils.mosaic
import com.ly.chatcompose.utils.save2Gallery
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
                            Box(modifier = Modifier
                                .size(50.dp)
                                .background(Color.Cyan)
                                .clickable {
                                    writePermission.launchPermissionRequest()
                                }) {

                            }
                            AndroidView(factory = {
                                val imageView = AppCompatImageView(it)
                                val bitmap = BitmapFactory.decodeResource(
                                    it.resources,
                                    R.drawable.someone_else
                                )
                                mosaic =
                                    bitmap.mosaic(dp2px(10f), 0, 0, bitmap.width, bitmap.height)
                                imageView.setImageBitmap(mosaic)
                                return@AndroidView imageView
                            }, modifier = Modifier.clickable {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(
                                        this,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 11
                                    )
                                } else {
                                    if (mosaic != null) {
                                        mosaic?.save2Gallery("mosaicPic")
                                    }
                                }

                            })

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
