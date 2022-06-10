package com.depromeet.baton.presentation.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun dateFormatUtil(date :String):String{
    val pattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
    val fm = SimpleDateFormat(pattern)
    fm.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    val formatDate= fm.parse(date)
    val convertFormat = SimpleDateFormat("yyyy-MM-dd")
    val convertedStr= convertFormat.format(formatDate)
    return convertedStr
}