package com.ly.wanandroid.page.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ly.wanandroid.ValueSetter
import com.ly.wanandroid.model.Article
import com.ly.wanandroid.ui.theme.WanAndroidTheme


@Composable
fun ItemArticle(
    article: Article,
    modifier: Modifier = Modifier,
    collect: ValueSetter<Article> = {},
    uncollect: ValueSetter<Article> = {}
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {

        }
        .padding(16.dp)
    ) {
        Column {

            Row(horizontalArrangement = Arrangement.SpaceAround) {

            }
        }
    }

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

private val testArticle = Article(
    audit = 1,
    canEdit = false,
    chapterId = 543,
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
    superChapterId = 543,
    superChapterName = "技术周报",
    title = "Android 技术周刊 （2021-11-10 ~ 2021-11-17）",
)
