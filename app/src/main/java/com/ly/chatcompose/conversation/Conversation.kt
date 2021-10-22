package com.ly.chatcompose.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.ly.chatcompose.FunctionalityNotAvailablePopup
import com.ly.chatcompose.R
import com.ly.chatcompose.ValueSetter
import com.ly.chatcompose.VoidCallback
import com.ly.chatcompose.components.CAppBar
import com.ly.chatcompose.data.initialMessages
import com.ly.chatcompose.ui.theme.ChatComposeTheme
import com.ly.chatcompose.ui.theme.elevatedSurface
import kotlinx.coroutines.launch


@Composable
fun ChannelNameBar(
    modifier: Modifier = Modifier,
    channelName: String,
    channelMembers: Int,
    onNavIconPressed: VoidCallback = {},
) {
    var functionalityNotAvailablePopupShown by remember {
        mutableStateOf(false)
    }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }
    CAppBar(modifier = modifier, onNavIconPress = onNavIconPressed, title = {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = channelName, style = MaterialTheme.typography.subtitle1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.members, channelMembers),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }, actions = {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "search",
                modifier = Modifier
                    .clickable {
                        functionalityNotAvailablePopupShown = true
                    }
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp)
            )
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "info",
                modifier = Modifier
                    .clickable {
                        functionalityNotAvailablePopupShown = true
                    }
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp)
            )
        }
    })
}

@Composable
fun Messages(
    messages: List<Message>,
    navigateTopProfile: ValueSetter<String>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {
        val authorMe = stringResource(id = R.string.author_me)
        LazyColumn(
            reverseLayout = true, state = scrollState, contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalTop = 90.dp
            ), modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author

                if (index == messages.size - 1) {
                    item {
                        DayHeader(dayString = "20 Aug")
                    }
                } else if (index == 2) {
                    item {
                        DayHeader(dayString = "Today")
                    }
                }
                item {
                    Message(
                        onAuthorClick = { navigateTopProfile(it) },
                        msg = content,
                        isUserMe = content.author == authorMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        val jumToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 || scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }
        JumpToBottom(enabled = jumToBottomButtonEnabled, onClicked = {
            scope.launch {
                scrollState.animateScrollToItem(0)
            }
        }, modifier = Modifier.align(BottomCenter))
    }
}


@Composable
fun Message(
    onAuthorClick: ValueSetter<String>,
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {

    val borderColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    val spaceBetweenAuthors =
        if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            //avatar
            Image(
                painter = painterResource(id = msg.authorImage),
                contentDescription = msg.author,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clickable { onAuthorClick(msg.author) }
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top)
            )
        } else {
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: ValueSetter<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg = msg)
        }
        ChatItemBubble(
            message = msg,
            lastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = authorClicked
        )
        if (isFirstMessageByAuthor) {
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .alignBy(
                    LastBaseline
                )
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = msg.timestamp,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.alignBy(
                    LastBaseline
                )
            )
        }
    }
}

@Composable
fun DayHeader(dayString: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        DayHeaderLine()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = dayString,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.overline
            )
        }
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(CenterVertically),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun ChatItemBubble(
    message: Message,
    lastMessageByAuthor: Boolean,
    authorClicked: ValueSetter<String>
) {
    val backgroundBubbleColor = if (MaterialTheme.colors.isLight) {
        Color(0xfff5f5f5)
    } else {
        MaterialTheme.colors.elevatedSurface(elevation = 2.dp)
    }

    val bubbleShape = if (lastMessageByAuthor) LastChatBubbleShape else ChatBubbleShape
    Column {
        Surface(color = backgroundBubbleColor, shape = bubbleShape) {
            ClickableMessage(message = message, authorClicked = authorClicked)
        }
        message.image?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(color = backgroundBubbleColor, shape = bubbleShape) {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = stringResource(id = R.string.attached_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp)
                )
            }
        }
    }


}

@Composable
fun ClickableMessage(message: Message, authorClicked: ValueSetter<String>) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(text = message.content)

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.body1.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(8.dp),
        onClick = {
            styledMessage.getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(uri = annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        })
}


@Preview
@Composable
fun ClickableMessage() {
    ChatComposeTheme {
        Surface {
            Column {
                val scrollState = rememberLazyListState()
                Messages(messages = initialMessages, navigateTopProfile = {

                }, scrollState = scrollState)
            }
        }
    }
}

private val JumpToBottomThreshold = 56.dp

private fun ScrollState.atBottom(): Boolean = value == 0

private val ChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)

private val LastChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)

const val ConversationTestTag = "ConversationTestTag"