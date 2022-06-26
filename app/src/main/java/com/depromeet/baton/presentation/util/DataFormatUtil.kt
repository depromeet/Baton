package com.depromeet.baton.presentation.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.ceil


fun priceFormat(price: Float): String {
    val dec = DecimalFormat("###,###")
    return dec.format(price)
}

fun priceFormatWithZero(price: Float): String {
    val priceStr = price.toInt().toString()
    val dec = DecimalFormat("#,###")

    return if (priceStr.length >= 5)
        dec.format((priceStr.substring(0, priceStr.length - 3) + "000").toInt())
    else "0"
}

fun ceilAndToStringFormat(data: Float): String {
    return ceil(data).toInt().toString()
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun dateDifferenceFormat(endDate: String): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    val startDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).replace("-", "")
    return (((dateFormat.parse(endDate)?.time ?: 0) - (dateFormat.parse(startDate)?.time ?: 0)) / (24 * 60 * 60 * 1000)).toString()
}

fun dateFormatUtil(date: String): String {
    try {
        val pattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val fm = SimpleDateFormat(pattern)
        fm.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        val formatDate = fm.parse(date)
        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
        val convertedStr = convertFormat.format(formatDate)
        return convertedStr
    } catch (e: Exception) {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss"
        val fm = SimpleDateFormat(pattern)
        fm.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        val formatDate = fm.parse(date)
        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
        val convertedStr = convertFormat.format(formatDate)
        return convertedStr
    } catch (e2: Exception) {
        Timber.e(e2.message)
    }
    return date
}

@SuppressLint("SimpleDateFormat")
fun getDayByDate(date: String): String {
    val pattern = "yyyy.MM.dd"
    val fm = SimpleDateFormat(pattern)
    fm.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    val formatDate = fm.parse(date)

    val calendar = Calendar.getInstance()
    if (formatDate != null) {
        calendar.time = formatDate
    }

    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "일"
        2 -> "월"
        3 -> "화"
        4 -> "수"
        5 -> "목"
        6 -> "금"
        7 -> "토"
        else -> " "
    }
}

