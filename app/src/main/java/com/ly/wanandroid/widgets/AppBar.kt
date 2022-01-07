package com.ly.wanandroid.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsPadding
import com.ly.wanandroid.ComposableCallback
import com.ly.wanandroid.ComposableExtCallback
import com.ly.wanandroid.R
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.mainOrSurface

val WAppBarHeight = 56.dp

@Composable
fun WAppBar(
    title: String = "",
    titleColor: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colors.mainOrSurface,
    navigationIcon: ComposableCallback = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseAppbar(
        navigationIcon = navigationIcon,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = actions,
        modifier = Modifier
            .background(backgroundColor)
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(AppBarDefaults.ContentPadding)
            .height(WAppBarHeight),
    )


}

@Composable
fun WAppBar(
    backgroundColor: Color = MaterialTheme.colors.mainOrSurface,
    title: ComposableCallback = {},
    navigationIcon: ComposableCallback = {},
    actions: ComposableExtCallback<RowScope> = {}
) {
    BaseAppbar(
        navigationIcon = navigationIcon, title = title, actions = actions,
        modifier = Modifier
            .background(backgroundColor)
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(AppBarDefaults.ContentPadding)
            .height(WAppBarHeight),
    )
}

@Composable
fun CommonAppBar(
    title: String = "",
    titleColor: Color = Color.White,
    backPress: VoidCallback,
    backgroundColor: Color = MaterialTheme.colors.mainOrSurface,
    actions: ComposableExtCallback<RowScope>,
) {
    WAppBar(
        title = title,
        titleColor = titleColor,
        backgroundColor = backgroundColor,
        actions = actions,
        navigationIcon = {
            IconButton(onClick = backPress) {
                Icon(
                    imageVector = Icons.Sharp.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colors.mainOrSurface
                )
            }
        })
}


@Composable
private fun BaseAppbar(
    modifier: Modifier = Modifier,
    navigationIcon: ComposableCallback,
    title: ComposableCallback,
    actions: ComposableExtCallback<RowScope>
) {
    Layout(modifier = modifier,
        content = {
            Box {
                navigationIcon()
            }
            Box {
                title()
            }
            Row(
                content = actions,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.sizeIn()
            )
        }, measurePolicy = { measurables, constraints ->
            //把绝对约束改成宽松约束
            val constraints1 = constraints.copy(minWidth = 0, minHeight = 0)
            val navigationIconPlaceable = measurables[0].measure(constraints1)
            val actionsPlaceable = measurables[2].measure(constraints1)
            //标题区域真正可用的约束
            val titleRealConstraint = Constraints(
                minWidth = 0,
                maxWidth = constraints1.maxWidth - navigationIconPlaceable.width - actionsPlaceable.width,
                minHeight = 0,
                maxHeight = constraints1.maxHeight
            )
            val titlePlaceable = measurables[1].measure(titleRealConstraint)
            val navIconOffsetY = (constraints.maxHeight - navigationIconPlaceable.height) / 2
            val actionsOffsetY = (constraints.maxHeight - actionsPlaceable.height) / 2
            val titleOffsetY = (constraints.maxHeight - titlePlaceable.height) / 2
            val leftCanUsedWidth = constraints.maxWidth / 2 - navigationIconPlaceable.width
            val rightCanUsedWidth = constraints.maxWidth / 2 - actionsPlaceable.width
            val titleWidthHalf = titlePlaceable.width / 2
            val defaultTitleOffsetX = constraints.maxWidth / 2 - titlePlaceable.width / 2
            val titleOffsetX = when {
                navigationIconPlaceable.width == 0 && actionsPlaceable.width == 0 -> defaultTitleOffsetX
                navigationIconPlaceable.width > 0 && actionsPlaceable.width == 0 -> {
                    if (titleWidthHalf > leftCanUsedWidth) {
                        navigationIconPlaceable.width
                    } else {
                        defaultTitleOffsetX
                    }
                }
                navigationIconPlaceable.width == 0 && actionsPlaceable.width > 0 -> {
                    if (titleWidthHalf > rightCanUsedWidth) {
                        constraints.maxWidth - (actionsPlaceable.width + titlePlaceable.width)
                    } else {
                        defaultTitleOffsetX
                    }
                }
                else -> {
                    defaultTitleOffsetX
                }
            }
            val navigationIconOffset = IntOffset(0, navIconOffsetY)
            val actionsOffset = IntOffset(
                constraints.maxWidth - actionsPlaceable.width,
                actionsOffsetY
            )
            val titleOffset = IntOffset(titleOffsetX, titleOffsetY)
            layout(constraints1.maxWidth, constraints1.maxHeight) {
                navigationIconPlaceable.place(navigationIconOffset)
                actionsPlaceable.place(actionsOffset)
                titlePlaceable.place(titleOffset)
            }
        })
}