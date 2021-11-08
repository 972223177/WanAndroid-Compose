package com.ly.wanandroid.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.ly.wanandroid.FunctionalityNotAvailablePopup
import com.ly.wanandroid.R
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.components.AnimatingFabContent
import com.ly.wanandroid.components.CAppBar
import com.ly.wanandroid.components.baselineHeight
import com.ly.wanandroid.data.ProfileScreenState
import com.ly.wanandroid.data.meProfile
import com.ly.wanandroid.ui.theme.ChatComposeTheme

@Composable
fun ProfileScreen(userData: ProfileScreenState, onNavIconPressed: VoidCallback = {}) {
    var functionalityNotAvailablePopupShown by remember {
        mutableStateOf(false)
    }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        CAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            onNavIconPress = onNavIconPressed,
            actions = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert, contentDescription = stringResource(
                            id = R.string.more_options
                        ), modifier = Modifier
                            .clickable {
                                functionalityNotAvailablePopupShown = true
                            }
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .height(24.dp)
                    )
                }
            },
            title = {},
        )
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    ProfileHeader(
                        scrollState = scrollState,
                        data = userData,
                        containerHeight = this@BoxWithConstraints.maxHeight
                    )
                    UserInfoFields(
                        userData = userData,
                        containerHeight = this@BoxWithConstraints.maxHeight
                    )
                }
            }
            ProfileFab(
                extended = scrollState.value == 0,
                userIsMe = userData.isMe(),
                modifier = Modifier.align(
                    Alignment.BottomEnd
                ),
                onFabClick = { functionalityNotAvailablePopupShown = true }
            )
        }
    }
}

@Composable
private fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.heightIn(8.dp))
        NameAndPosition(userData = userData)
        ProfileProperty(
            label = stringResource(id = R.string.display_name),
            value = userData.displayName
        )
        ProfileProperty(label = stringResource(id = R.string.status), value = userData.status)
        ProfileProperty(label = stringResource(id = R.string.twitter), value = userData.twitter)
        userData.timeZone?.let {
            ProfileProperty(
                label = stringResource(id = R.string.timezone),
                value = userData.timeZone
            )
        }
        Spacer(modifier = Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition(userData: ProfileScreenState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(userData = userData, modifier = Modifier.baselineHeight(32.dp))
        Position(
            userData = userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(text = userData.name, modifier = modifier, style = MaterialTheme.typography.h5)
}

@Composable
private fun Position(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(text = userData.position, modifier = modifier, style = MaterialTheme.typography.body1)
    }
}


@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = label,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }
        Text(text = value, modifier = Modifier.baselineHeight(24.dp), style = style)
    }
}

@Composable
private fun ProfileHeader(scrollState: ScrollState, data: ProfileScreenState, containerHeight: Dp) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }
    data.photo?.let {
        Image(
            painter = painterResource(id = it),
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth()
                .padding(top = offsetDp)
        )
    }
}

@Composable
fun ProfileError() {
    Text(text = stringResource(id = R.string.profile_error))
}

@Composable
fun ProfileFab(
    extended: Boolean,
    userIsMe: Boolean,
    modifier: Modifier = Modifier,
    onFabClick: VoidCallback = {}
) {
    key(userIsMe) {
        FloatingActionButton(
            onClick = onFabClick,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector = if (userIsMe) Icons.Outlined.Create else Icons.Outlined.Chat,
                        contentDescription = stringResource(id = if (userIsMe) R.string.edit_profile else R.string.message)
                    )
                },
                text = {
                    Text(text = stringResource(id = if (userIsMe) R.string.edit_profile else R.string.message))
                },
                extended = extended,
            )
        }
    }
}


@Preview(widthDp = 340, name = "340 width -me")
@Composable
fun ProfileScreenPre340() {
    ChatComposeTheme {
        ProfileScreen(userData = meProfile)
    }
}

@Preview
@Composable
fun ProfileTabPre() {
    ChatComposeTheme {
        Surface {
            ProfileFab(extended = true, userIsMe = false)
        }
    }
}