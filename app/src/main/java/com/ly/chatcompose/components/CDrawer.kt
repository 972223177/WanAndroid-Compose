package com.ly.chatcompose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import com.ly.chatcompose.R
import com.ly.chatcompose.data.colleagueProfile
import com.ly.chatcompose.data.meProfile
import com.ly.chatcompose.ui.theme.ChatComposeTheme

@Composable
fun ColumnScope.CDrawer(onProfileClicked: (String) -> Unit, onChatClicked: (String) -> Unit) {
    Spacer(modifier = Modifier.statusBarsHeight())
    DrawerHeader()
    Divider()
    DrawerItemHeader(text = "Chats")
    ChatItem(text = "Chat1", selected = true) {
        onChatClicked("Chat1")
    }
    ChatItem(text = "Chat2", selected = false) {
        onChatClicked("Chat2")
    }
    DrawerItemHeader(text = "Recent Profiles")
    ProfileItem(text = "Profile1", profilePic = meProfile.photo) {
        onProfileClicked(meProfile.userId)
    }
    ProfileItem(text = "Profile2", profilePic = colleagueProfile.photo) {
        onProfileClicked(colleagueProfile.userId)
    }

}

@Composable
private fun DrawerHeader() {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_jetchat),
            contentDescription = "DrawerHeaderIcon",
            modifier = Modifier.size(24.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.jetchat_logo),
            contentDescription = "DrawerHeaderLogo",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun DrawerItemHeader(text: String) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ChatItem(text: String, selected: Boolean, onChatClicked: () -> Unit) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.08f))
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(background)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onChatClicked)
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_jetchat),
            contentDescription = "ChatItem:$text",
            modifier = Modifier.padding(8.dp),
            tint = iconTint
        )
        CompositionLocalProvider(values = arrayOf(LocalContentAlpha provides ContentAlpha.medium)) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = if (selected) MaterialTheme.colors.primary else LocalContentColor.current,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun ProfileItem(text: String, @DrawableRes profilePic: Int?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 9.dp, vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onProfileClicked)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val widthPaddingModifier = Modifier
                .padding(8.dp)
                .size(24.dp)
            if (profilePic != null) {
                Image(
                    painter = painterResource(id = profilePic),
                    contentDescription = "profilePic :$text",
                    modifier = widthPaddingModifier.then(Modifier.clip(CircleShape)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Spacer(modifier = widthPaddingModifier)
            }
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview
@Composable
fun ProfileItemPre() {
    ChatComposeTheme(false) {
        Surface {
            ProfileItem(text = "ProfileItemPrv", profilePic = R.drawable.ic_baseline_person_24) {

            }
        }
    }
}

@Preview
@Composable
fun ChatItemPre() {
    ChatComposeTheme(false) {
        Surface {
            ChatItem(text = "ChatItemPre", selected = false) {

            }
        }
    }
}

@Preview
@Composable
fun DrawerHeaderPre() {
    ChatComposeTheme {
        Surface {
            DrawerHeader()
        }
    }
}

@Preview
@Composable
fun DrawerPre() {
    ChatComposeTheme {
        Surface {
            Column {
                CDrawer(onProfileClicked = {}, onChatClicked = {})
            }
        }
    }
}