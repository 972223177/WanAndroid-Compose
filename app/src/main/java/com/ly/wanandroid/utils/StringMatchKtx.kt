package com.ly.wanandroid.utils


private const val REGEX_EMAIL =
    "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$"
private const val REGEX_PHONE = "^(1[3456789][0-9])\\d{8}"

private const val REGEX_PHONE_HIDE = "(\\d{3})\\d{4}(\\d{4})"
private const val REGEX_EMAIL_HIDE = "(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)"

private const val REGEX_ID_NUM = "^\\d{15}|\\d{18}|\\d{17}(\\d|X|x)"

fun String.isEmail(): Boolean = matches(Regex(REGEX_EMAIL))

fun String.isPhone(): Boolean = matches(Regex(REGEX_PHONE))

fun String.idCardLegal(): Boolean = matches(Regex(REGEX_ID_NUM))

/**
 * 隐藏中间几个号码
 */
fun String.hidePhone(): String {
    return replace(Regex(REGEX_PHONE_HIDE)) {
        return@replace "*"
    }
}

fun String.hideEmail(): String = replace(Regex(REGEX_EMAIL_HIDE)) {
    return@replace "*"
}


