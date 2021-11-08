package com.ly.wanandroid.conversation

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ly.wanandroid.FunctionalityNotAvailablePopup
import com.ly.wanandroid.R
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.VoidCallback
import com.ly.wanandroid.ui.theme.ChatComposeTheme
import com.ly.wanandroid.ui.theme.compositedOnSurface
import com.ly.wanandroid.ui.theme.elevatedSurface

enum class InputSelector {
    NONE, MAP, DM, EMOJI, PHONE, PICTURE
}

enum class EmojiStickerSelector {
    EMOJI, STICKER
}

@ExperimentalAnimationApi
@Composable
fun UserInput(
    onMessageSend: ValueSetter<String>,
    modifier: Modifier = Modifier,
    resetScroll: VoidCallback = {}
) {
    var currentInputSelector by rememberSaveable {
        mutableStateOf(InputSelector.NONE)
    }
    val dismissKeyboard = {
        currentInputSelector = InputSelector.NONE
    }

    if (currentInputSelector != InputSelector.NONE) {
        BackPressHandler(onBackPressed = dismissKeyboard)
    }

    var textState by remember {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        Divider()
        UserInputText(
            onTextChanged = { textState = it },
            textFieldValue = textState,
            keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                    resetScroll()
                }
                textFieldFocusState = focused
            },
            focusState = textFieldFocusState
        )
        UserInputSelector(
            onSelectorChange = { currentInputSelector = it },
            sendMessageEnabled = textState.text.isNotBlank(),
            onMessageSent = {
                onMessageSend(textState.text)
                textState = TextFieldValue()
                resetScroll()
                dismissKeyboard()
            },
            currentInputSelector = currentInputSelector
        )
        SelectorExpanded(
            currentSelector = currentInputSelector,
            onCloseRequested = dismissKeyboard,
            onTextAdded = { textState = textState.addText(it) },
        )

    }

}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = text.replaceRange(this.selection.start, this.selection.end, newString)
    val newSelection = TextRange(start = newText.length, end = newText.length)
    return copy(text = newText, selection = newSelection)
}

@ExperimentalAnimationApi
@Composable
fun SelectorExpanded(
    currentSelector: InputSelector,
    onCloseRequested: VoidCallback,
    onTextAdded: ValueSetter<String>
) {
    if (currentSelector == InputSelector.NONE) return
    val focusRequester = FocusRequester()
    SideEffect {
        if (currentSelector == InputSelector.EMOJI) {
            focusRequester.requestFocus()
        }
    }
    val selectorExpandedColor = getSelectorExpandedColor()
    Surface(color = selectorExpandedColor, elevation = 3.dp) {
        when (currentSelector) {
            InputSelector.MAP -> FunctionalityNotAvailablePanel()
            InputSelector.DM -> NotAvailablePopup(onCloseRequested)
            InputSelector.EMOJI -> EmojiSelector(
                onTextAdded = onTextAdded,
                focusRequester = focusRequester
            )
            InputSelector.PHONE -> FunctionalityNotAvailablePanel()
            InputSelector.PICTURE -> FunctionalityNotAvailablePanel()
            else -> throw  NotImplementedError()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun FunctionalityNotAvailablePanel() {
    AnimatedVisibility(
        visibleState = remember {
            MutableTransitionState(false).apply { targetState = true }
        },
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.not_available),
                style = MaterialTheme.typography.subtitle1
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.not_available_subtitle),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.paddingFrom(FirstBaseline, before = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun UserInputSelector(
    onSelectorChange: ValueSetter<InputSelector>,
    sendMessageEnabled: Boolean,
    onMessageSent: VoidCallback,
    currentInputSelector: InputSelector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .wrapContentHeight()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.EMOJI) },
            icon = Icons.Outlined.Mood,
            description = stringResource(id = R.string.emoji_selector_bt_desc),
            selected = currentInputSelector == InputSelector.EMOJI
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.DM) },
            icon = Icons.Outlined.AlternateEmail,
            description = stringResource(id = R.string.dm_desc),
            selected = currentInputSelector == InputSelector.DM
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.PICTURE) },
            icon = Icons.Outlined.InsertPhoto,
            description = stringResource(id = R.string.attach_photo_desc),
            selected = currentInputSelector == InputSelector.PICTURE
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.MAP) },
            icon = Icons.Outlined.Place,
            description = stringResource(id = R.string.map_selector_desc),
            selected = currentInputSelector == InputSelector.MAP
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.PHONE) },
            icon = Icons.Outlined.Duo,
            description = stringResource(
                id = R.string.videochat_desc
            ),
            selected = currentInputSelector == InputSelector.PHONE
        )
        Spacer(modifier = Modifier.weight(1f))
        val border = if (!sendMessageEnabled) {
            BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        } else {
            null
        }
        val disabledContentColor =
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        val buttonColors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.surface,
            disabledContentColor = disabledContentColor
        )
        Button(
            onClick = onMessageSent,
            enabled = sendMessageEnabled,
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = buttonColors,
            contentPadding = PaddingValues(0.dp),
            border = border
        ) {
            Text(
                text = stringResource(id = R.string.send),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

}

@Composable
fun InputSelectorButton(
    onClick: VoidCallback,
    icon: ImageVector,
    description: String,
    selected: Boolean
) {
    IconButton(onClick = onClick) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val tint = if (selected) MaterialTheme.colors.primary else LocalContentColor.current
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = tint,
                modifier = Modifier
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun NotAvailablePopup(onDismissed: VoidCallback) {
    FunctionalityNotAvailablePopup(onDismissed)
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    val a11yLabel = stringResource(id = R.string.textfield_desc)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .semantics {
                contentDescription = a11yLabel
                keyboardShownProperty = keyboardShown
            },
        horizontalArrangement = Arrangement.End
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .align(Alignment.Bottom)
            ) {
                var lastFocusState by remember { mutableStateOf(false) }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                        .onFocusChanged { state ->
                            if (lastFocusState != state.isFocused) {
                                onTextFieldFocused(state.isFocused)
                            }
                            lastFocusState = state.isFocused
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Send
                    ),
                    maxLines = 1,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
                )

                val disableContentColor =
                    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                if (textFieldValue.text.isEmpty() && !focusState) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp),
                        text = stringResource(id = R.string.textfield_hint),
                        style = MaterialTheme.typography.body1.copy(color = disableContentColor)
                    )
                }
            }
        }
    }
}

@Composable
fun EmojiSelector(onTextAdded: ValueSetter<String>, focusRequester: FocusRequester) {
    var selected by remember {
        mutableStateOf(EmojiStickerSelector.EMOJI)
    }
    val a11yLabel = stringResource(id = R.string.emoji_selector_desc)
    Column(modifier = Modifier
        .focusRequester(focusRequester)
        .focusTarget()
        .semantics { contentDescription = a11yLabel }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            ExtendedSelectorInnerButton(
                text = stringResource(id = R.string.emojis_label),
                onClick = {
                    selected = EmojiStickerSelector.EMOJI
                },
                selected = true,
                modifier = Modifier.weight(1f)
            )
            ExtendedSelectorInnerButton(
                text = stringResource(id = R.string.stickers_label),
                onClick = {
                    selected = EmojiStickerSelector.STICKER
                },
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            EmojiTable(onTextAdded = onTextAdded, modifier = Modifier.padding(8.dp))
        }
    }
    if (selected == EmojiStickerSelector.STICKER) {
        NotAvailablePopup {
            selected = EmojiStickerSelector.EMOJI
        }
    }
}

@Composable
fun getSelectorExpandedColor(): Color {
    return if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.compositedOnSurface(alpha = 0.04f)
    } else {
        MaterialTheme.colors.elevatedSurface(elevation = 8.dp)
    }
}

@Composable
fun ExtendedSelectorInnerButton(
    text: String,
    onClick: VoidCallback,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val colors =
        ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.08f),
            disabledBackgroundColor = getSelectorExpandedColor(),
            contentColor = MaterialTheme.colors.onSurface,
            disabledContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f)
        )
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(30.dp),
        shape = MaterialTheme.shapes.medium,
        enabled = selected,
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text, style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun EmojiTable(onTextAdded: ValueSetter<String>, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        repeat(4) { x ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(EMOJI_COLUMNS) { y ->
                    val emoji = emojis[x * EMOJI_COLUMNS + y]
                    Text(text = emoji,
                        modifier = Modifier
                            .clickable { onTextAdded(emoji) }
                            .sizeIn(minWidth = 42.dp, minHeight = 42.dp)
                            .padding(8.dp),
                        style = LocalTextStyle.current.copy(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun UserInputPre() {
    ChatComposeTheme {
        Surface {
            UserInput(onMessageSend = {})
        }
    }
}

@Preview
@Composable
fun EmojiSelectorPre() {
    ChatComposeTheme {
        Surface {
            val requester by remember {
                mutableStateOf(FocusRequester())
            }
            EmojiSelector(onTextAdded = {}, focusRequester = requester)
        }
    }
}

@Preview
@Composable
fun ExtendedSelectorInnerButtonPre() {
    ChatComposeTheme {
        Surface {
            ExtendedSelectorInnerButton(text = "Extended", onClick = {}, selected = true)
        }
    }
}

@Preview
@Composable
fun EmojiTablePre() {
    ChatComposeTheme {
        Surface {
            EmojiTable(onTextAdded = {})
        }
    }
}

private const val EMOJI_COLUMNS = 10
private val emojis = listOf(
    "\ud83d\ude00", // Grinning Face
    "\ud83d\ude01", // Grinning Face With Smiling Eyes
    "\ud83d\ude02", // Face With Tears of Joy
    "\ud83d\ude03", // Smiling Face With Open Mouth
    "\ud83d\ude04", // Smiling Face With Open Mouth and Smiling Eyes
    "\ud83d\ude05", // Smiling Face With Open Mouth and Cold Sweat
    "\ud83d\ude06", // Smiling Face With Open Mouth and Tightly-Closed Eyes
    "\ud83d\ude09", // Winking Face
    "\ud83d\ude0a", // Smiling Face With Smiling Eyes
    "\ud83d\ude0b", // Face Savouring Delicious Food
    "\ud83d\ude0e", // Smiling Face With Sunglasses
    "\ud83d\ude0d", // Smiling Face With Heart-Shaped Eyes
    "\ud83d\ude18", // Face Throwing a Kiss
    "\ud83d\ude17", // Kissing Face
    "\ud83d\ude19", // Kissing Face With Smiling Eyes
    "\ud83d\ude1a", // Kissing Face With Closed Eyes
    "\u263a", // White Smiling Face
    "\ud83d\ude42", // Slightly Smiling Face
    "\ud83e\udd17", // Hugging Face
    "\ud83d\ude07", // Smiling Face With Halo
    "\ud83e\udd13", // Nerd Face
    "\ud83e\udd14", // Thinking Face
    "\ud83d\ude10", // Neutral Face
    "\ud83d\ude11", // Expressionless Face
    "\ud83d\ude36", // Face Without Mouth
    "\ud83d\ude44", // Face With Rolling Eyes
    "\ud83d\ude0f", // Smirking Face
    "\ud83d\ude23", // Persevering Face
    "\ud83d\ude25", // Disappointed but Relieved Face
    "\ud83d\ude2e", // Face With Open Mouth
    "\ud83e\udd10", // Zipper-Mouth Face
    "\ud83d\ude2f", // Hushed Face
    "\ud83d\ude2a", // Sleepy Face
    "\ud83d\ude2b", // Tired Face
    "\ud83d\ude34", // Sleeping Face
    "\ud83d\ude0c", // Relieved Face
    "\ud83d\ude1b", // Face With Stuck-Out Tongue
    "\ud83d\ude1c", // Face With Stuck-Out Tongue and Winking Eye
    "\ud83d\ude1d", // Face With Stuck-Out Tongue and Tightly-Closed Eyes
    "\ud83d\ude12", // Unamused Face
    "\ud83d\ude13", // Face With Cold Sweat
    "\ud83d\ude14", // Pensive Face
    "\ud83d\ude15", // Confused Face
    "\ud83d\ude43", // Upside-Down Face
    "\ud83e\udd11", // Money-Mouth Face
    "\ud83d\ude32", // Astonished Face
    "\ud83d\ude37", // Face With Medical Mask
    "\ud83e\udd12", // Face With Thermometer
    "\ud83e\udd15", // Face With Head-Bandage
    "\u2639", // White Frowning Face
    "\ud83d\ude41", // Slightly Frowning Face
    "\ud83d\ude16", // Confounded Face
    "\ud83d\ude1e", // Disappointed Face
    "\ud83d\ude1f", // Worried Face
    "\ud83d\ude24", // Face With Look of Triumph
    "\ud83d\ude22", // Crying Face
    "\ud83d\ude2d", // Loudly Crying Face
    "\ud83d\ude26", // Frowning Face With Open Mouth
    "\ud83d\ude27", // Anguished Face
    "\ud83d\ude28", // Fearful Face
    "\ud83d\ude29", // Weary Face
    "\ud83d\ude2c", // Grimacing Face
    "\ud83d\ude30", // Face With Open Mouth and Cold Sweat
    "\ud83d\ude31", // Face Screaming in Fear
    "\ud83d\ude33", // Flushed Face
    "\ud83d\ude35", // Dizzy Face
    "\ud83d\ude21", // Pouting Face
    "\ud83d\ude20", // Angry Face
    "\ud83d\ude08", // Smiling Face With Horns
    "\ud83d\udc7f", // Imp
    "\ud83d\udc79", // Japanese Ogre
    "\ud83d\udc7a", // Japanese Goblin
    "\ud83d\udc80", // Skull
    "\ud83d\udc7b", // Ghost
    "\ud83d\udc7d", // Extraterrestrial Alien
    "\ud83e\udd16", // Robot Face
    "\ud83d\udca9", // Pile of Poo
    "\ud83d\ude3a", // Smiling Cat Face With Open Mouth
    "\ud83d\ude38", // Grinning Cat Face With Smiling Eyes
    "\ud83d\ude39", // Cat Face With Tears of Joy
    "\ud83d\ude3b", // Smiling Cat Face With Heart-Shaped Eyes
    "\ud83d\ude3c", // Cat Face With Wry Smile
    "\ud83d\ude3d", // Kissing Cat Face With Closed Eyes
    "\ud83d\ude40", // Weary Cat Face
    "\ud83d\ude3f", // Crying Cat Face
    "\ud83d\ude3e", // Pouting Cat Face
    "\ud83d\udc66", // Boy
    "\ud83d\udc67", // Girl
    "\ud83d\udc68", // Man
    "\ud83d\udc69", // Woman
    "\ud83d\udc74", // Older Man
    "\ud83d\udc75", // Older Woman
    "\ud83d\udc76", // Baby
    "\ud83d\udc71", // Person With Blond Hair
    "\ud83d\udc6e", // Police Officer
    "\ud83d\udc72", // Man With Gua Pi Mao
    "\ud83d\udc73", // Man With Turban
    "\ud83d\udc77", // Construction Worker
    "\u26d1", // Helmet With White Cross
    "\ud83d\udc78", // Princess
    "\ud83d\udc82", // Guardsman
    "\ud83d\udd75", // Sleuth or Spy
    "\ud83c\udf85", // Father Christmas
    "\ud83d\udc70", // Bride With Veil
    "\ud83d\udc7c", // Baby Angel
    "\ud83d\udc86", // Face Massage
    "\ud83d\udc87", // Haircut
    "\ud83d\ude4d", // Person Frowning
    "\ud83d\ude4e", // Person With Pouting Face
    "\ud83d\ude45", // Face With No Good Gesture
    "\ud83d\ude46", // Face With OK Gesture
    "\ud83d\udc81", // Information Desk Person
    "\ud83d\ude4b", // Happy Person Raising One Hand
    "\ud83d\ude47", // Person Bowing Deeply
    "\ud83d\ude4c", // Person Raising Both Hands in Celebration
    "\ud83d\ude4f", // Person With Folded Hands
    "\ud83d\udde3", // Speaking Head in Silhouette
    "\ud83d\udc64", // Bust in Silhouette
    "\ud83d\udc65", // Busts in Silhouette
    "\ud83d\udeb6", // Pedestrian
    "\ud83c\udfc3", // Runner
    "\ud83d\udc6f", // Woman With Bunny Ears
    "\ud83d\udc83", // Dancer
    "\ud83d\udd74", // Man in Business Suit Levitating
    "\ud83d\udc6b", // Man and Woman Holding Hands
    "\ud83d\udc6c", // Two Men Holding Hands
    "\ud83d\udc6d", // Two Women Holding Hands
    "\ud83d\udc8f" // Kiss
)