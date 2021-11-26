package com.ly.wanandroid.page.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import com.ly.wanandroid.LocalNavController
import com.ly.wanandroid.R
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.model.Article
import com.ly.wanandroid.model.Tag
import com.ly.wanandroid.page.utils.toAnnotatedString
import com.ly.wanandroid.route.WebViewRouteArg
import com.ly.wanandroid.ui.NavRoute
import com.ly.wanandroid.ui.goWebView
import com.ly.wanandroid.ui.navTo
import com.ly.wanandroid.ui.navWithRouteArg
import com.ly.wanandroid.ui.theme.*
import com.ly.wanandroid.utils.logD
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ItemArticle(
    article: Article,
    modifier: Modifier = Modifier,
    collect: ValueSetter<Article> = {},
    uncollect: ValueSetter<Article> = {}
) {
    val navController = LocalNavController.current
    logD("article$article")
    Box(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {
            navController.goWebView(article.link)
        }
        .padding(16.dp)
    ) {
        Column {
            TopInfo(article = article)
            MiddleInfo(article = article)
            BottomInfo(article = article)
        }
    }
}

@Composable
private fun TopInfo(article: Article) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            if (article.fresh) {
                Text(
                    text = "新",
                    color = MaterialTheme.colors.textMain,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        end = 5.dp
                    )
                )
            }
            val name = if (article.author.isEmpty()) article.shareUser else article.author
            Text(
                text = name,
                color = MaterialTheme.colors.textSecond,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    //todo go personal page
                })
            if (article.tags.isNotEmpty()) {
                Text(
                    text = article.tags[0].name,
                    color = MaterialTheme.colors.textMain,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            //todo go knowledge page
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.main,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(horizontal = 3.dp)
                )
            }
        }
        Text(
            text = article.niceDate,
            color = MaterialTheme.colors.textThird,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun MiddleInfo(article: Article) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        if (article.envelopePic.isNotEmpty()) {
            val imagePainter = rememberImagePainter(
                data = article.envelopePic
            ) {
                placeholder(drawableResId = R.drawable.image_holder)
            }
            Image(
                painter = imagePainter,
                contentDescription = "envelopePic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(120.dp, 80.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
        }
        Column {
            Text(
                text = HtmlCompat.fromHtml(article.title, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toAnnotatedString(),
                fontSize = 15.sp,
                letterSpacing = 0.sp,
                maxLines = if (article.desc.isEmpty()) 3 else 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.textSurface
            )
            if (article.desc.isNotEmpty()) {
                Text(
                    text = HtmlCompat.fromHtml(article.desc, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        .toAnnotatedString(),
                    maxLines = 3,
                    fontSize = 13.sp,
                    letterSpacing = 0.sp,
                    color = MaterialTheme.colors.textSecond,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
private fun BottomInfo(article: Article) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Html.fromHtml(formatChapterName(item.getSuperChapterName(), item.getChapterName()))
        Row(modifier = Modifier.padding(top = 10.dp)) {
            if (article.top) {
                Text(
                    text = "置顶",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.textAccent,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
            Text(
                text = HtmlCompat.fromHtml(
                    formatChapterName(
                        article.superChapterName,
                        article.chapterName
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ).toAnnotatedString(),
                fontSize = 12.sp,
                color = MaterialTheme.colors.textThird,
                modifier = Modifier.clickable {
                    //todo go knowledge page
                }
            )
        }
        CollectView(article = article)
    }
}

@Composable
private fun CollectView(article: Article) {
    var isChecked by remember {
        mutableStateOf(article.collect)
    }
    val tint = if (isChecked) {
        Color.Cyan
    } else {
        if (MaterialTheme.colors.isLight) {
            Color.Black
        } else {
            Color.White
        }
    }
    Icon(
        imageVector = if (isChecked) Icons.Sharp.Favorite else Icons.Sharp.FavoriteBorder,
        contentDescription = if (isChecked) "collected" else "uncollect",
        tint = tint,
        modifier = Modifier
            .size(20.dp)
            .clickable {
                isChecked = !isChecked
                //todo  collect or uncollect
            }
    )
}

@Preview
@Composable
fun ItemArticlePre() {
    WanAndroidTheme {
        Surface {
            ItemArticle(article = testArticle)
        }
    }
}

private fun formatChapterName(vararg names: String): String {
    val format = StringBuilder("")
    names.forEach {
        if (it.isNotEmpty()) {
            if (format.isNotEmpty()) {
                format.append("·")
            }
            format.append(it)
        }
    }
    return format.toString()
}

private val testArticle = Article(
    audit = 1,
    canEdit = false,
    chapterId = 543,
    fresh = true,
    top = true,
    tags = listOf(Tag("项目", "")),
    author = "张洪洋",
    chapterName = "Android技术周报",
    courseId = 13,
    id = 20512,
    link = "https://www.wanandroid.com/blog/show/3097",
    niceDate = "2021-11-17 00:00",
    niceShareDate = "2021-11-17 00:10",
    publishTime = 1637078400000,
    realSuperChapterId = 542,
    selfVisible = 0,
    shareDate = 1637079000000,
    desc = "这个是描述",
    superChapterId = 543,
    superChapterName = "技术周报",
    title = "Android 技术周刊 （2021-11-10 ~ 2021-11-17）",
)
