package com.ly.chatcompose.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import com.ly.chatcompose.MainViewModel
import com.ly.chatcompose.ui.theme.ChatComposeTheme

class ProfileFragment : Fragment() {

    private val mViewModel by viewModels<ProfileViewModel>()
    private val mActivityViewMode by activityViewModels<MainViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val userId = arguments?.getString("userId")
        mViewModel.setUserId(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        val windowInset = ViewWindowInsetObserver(this).start()
        setContent {
            val userData by mViewModel.userData.observeAsState()
            CompositionLocalProvider(LocalWindowInsets provides windowInset) {
                ChatComposeTheme {
                    if (userData == null) {
                        ProfileError()
                    }
                }
            }
        }
    }
}