package com.ly.wanandroid.page.home.mine

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DarkMode
import androidx.compose.material.icons.sharp.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ly.wanandroid.R
import com.ly.wanandroid.config.setting.Setting
import com.ly.wanandroid.config.setting.User
import com.ly.wanandroid.domain.LoginData
import com.ly.wanandroid.ui.theme.*
import com.ly.wanandroid.base.widgets.WAppBar
import com.ly.wanandroid.base.widgets.WAppBarHeight

@Composable
fun MineScreen(viewModel: MineViewModel = hiltViewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            AppBarMine(viewModel = viewModel)
            Body(viewModel = viewModel)
        }
    }
}

@Composable
private fun Body(viewModel: MineViewModel) {
    val user by viewModel.user.collectAsState()
    LazyColumn {
        item {
            Head(user)
        }
        item {
            MenuItem(
                icon = painterResource(id = R.drawable.ic_coin),
                title = "我的积分",
                subTitle = if (user.coinCount > 0) "${user.coinCount}" else ""
            )
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_share), title = "我的分享")
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_collect), title = "我的分享")
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_read_later), title = "我的书签")
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_read_record), title = "阅读历史")
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_github), title = "开源项目")
        }
        item {
            MenuItem(icon = painterResource(id = R.drawable.ic_setting), title = "系统设置")
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AppBarMine(viewModel: MineViewModel) {
    WAppBar(actions = {
        IconButton(onClick = {
            Setting.openNightMode(!Setting.isNightMode)
        }) {
            Icon(
                imageVector = if (Setting.isNightMode) Icons.Sharp.DarkMode else Icons.Sharp.LightMode,
                contentDescription = "mode",
                tint = MaterialTheme.colors.onMainOrSurface
            )
        }
        Box {

            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = "notification",
                    tint = MaterialTheme.colors.onMainOrSurface
                )
            }
            val count = 1 //todo
            androidx.compose.animation.AnimatedVisibility(
                visible = count > 0,
                modifier = Modifier
                    .padding(end = 10.dp, top = 8.dp)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = "1",
                    color = MaterialTheme.colors.textOnMain,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.striking, shape = CircleShape
                        )
                        .height(15.dp)
                        .widthIn(min = 15.dp)
                )
            }
        }
    })
}


@Composable
private fun Head(user: LoginData) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp - WAppBarHeight)
            .background(MaterialTheme.colors.mainOrSurface),
        constraintSet = headContentConstraintSet
    ) {

        val avatarPainter = rememberImagePainter(data = user.icon)
        Image(
            painter = avatarPainter,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .layoutId(ConstraintId.IvAvatar)
                .size(80.dp)
                .background(color = MaterialTheme.colors.surface1Mask, shape = CircleShape)
        )
        Text(
            text = if (User.isLogin()) user.username else "去登陆",
            fontSize = 22.sp,
            color = MaterialTheme.colors.onMainOrSurface,

            modifier = Modifier.layoutId(ConstraintId.TvName)
        )
        Text(
            text = user.email,
            fontSize = 12.sp,
            color = MaterialTheme.colors.onMainOrSurface,
            modifier = Modifier.layoutId(ConstraintId.TvEmail)
        )
    }
}

@Composable
private fun MenuItem(
    icon: Painter,
    title: String,
    subTitle: String = "",
    clickable: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { clickable() }
            .padding(horizontal = 16.dp)
            .height(50.dp)

    ) {
        Icon(painter = icon, contentDescription = title, tint = MaterialTheme.colors.iconMain)
        Text(
            text = title, fontSize = 15.sp, color = MaterialTheme.colors.textSurface, maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
        )
        Text(
            text = subTitle,
            fontSize = 12.sp,
            color = MaterialTheme.colors.textSecond,
            modifier = Modifier.padding(end = 5.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_enter),
            contentDescription = "enter",
            tint = MaterialTheme.colors.iconThird
        )

    }
}

@Preview
@Composable
fun MinePre() {
    Surface {
        MenuItem(icon = painterResource(id = R.drawable.ic_coin), title = "我的积分", subTitle = "520")
    }
}


private val headContentConstraintSet by lazy(LazyThreadSafetyMode.NONE) {
    ConstraintSet {
        val avatar = createRefFor(ConstraintId.IvAvatar)
        val name = createRefFor(ConstraintId.TvName)
        val email = createRefFor(ConstraintId.TvEmail)
        constrain(avatar) {
            top.linkTo(parent.top, margin = 30.dp)
            centerHorizontallyTo(parent)
        }
        constrain(name) {
            top.linkTo(avatar.bottom, margin = 10.dp)
            centerHorizontallyTo(parent)
        }
        constrain(email) {
            top.linkTo(parent.top, margin = 10.dp)
            centerHorizontallyTo(parent)
        }
    }
}

private object ConstraintId {
    const val IvAvatar = "ivAvatar"
    const val TvName = "tvName"
    const val TvEmail = "tvEmail"

}