package com.ly.wanandroid.config.http

import com.ly.wanandroid.utils.preference

object HttpConstant {
    const val DEFAULT_TIMEOUT = 10L

    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"

    const val COLLECTIONS_WEBSITE = "lg/collect"
    const val UNCOLLECTIONS_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val TODO_WEBSITE = "lg/todo"
    const val COIN_WEBSITE = "lg/coin"

    const val SET_COOKIE_KEY = "set-cookie"

    const val COOKIE_NAME = "Cookie"

    const val TOKEN_KEY = "token"

    const val CACHE_SAVE_ID = "httpCache"

    const val MAX_CACHE_SIZE = 1024 * 1024 * 50 //50m

    val verifyCookieMethods by lazy {
        listOf(
            COLLECTIONS_WEBSITE, UNCOLLECTIONS_WEBSITE, ARTICLE_WEBSITE, TODO_WEBSITE,
            COIN_WEBSITE
        )
    }

    fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies.map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }.forEach {
            it.filterNot { value ->
                set.contains(value)
            }.forEach { value ->
                set.add(value)
            }
        }
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }


    fun saveCookie(url: String?, domain: String?, cookies: String) {
        if (url.isNullOrEmpty()) return
        var spUrl by preference(cookies, url)
        spUrl = cookies
        if (domain.isNullOrEmpty()) return
        var spDomain by preference(cookies, domain)
        spDomain = cookies
    }
}