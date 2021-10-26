package com.ly.chatcompose.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import com.google.accompanist.insets.navigationBarsPadding
import com.ly.chatcompose.MainViewModel
import com.ly.chatcompose.R
import com.ly.chatcompose.data.exampleUiState
import com.ly.chatcompose.ui.theme.ChatComposeTheme

class ConversationFragment : Fragment() {
    private val mViewModel by activityViewModels<MainViewModel>()



    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        val windowInsets = ViewWindowInsetObserver(this).start(windowInsetsAnimationsEnabled = true)
        setContent {
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides requireActivity().onBackPressedDispatcher,
                LocalWindowInsets provides windowInsets
            ) {
                ChatComposeTheme {
                    ConversationContent(
                        uiState = exampleUiState,
                        navigateToProfile = { user ->
                            val bundle = bundleOf("userId" to user)
                            findNavController().navigate(R.id.nav_profile, bundle)
                        },
                        onNavIconPressed = {
                            mViewModel.openDrawer()
                        },
                        modifier = Modifier.navigationBarsPadding(bottom = false),
                    )
                }
            }
        }

    }
}