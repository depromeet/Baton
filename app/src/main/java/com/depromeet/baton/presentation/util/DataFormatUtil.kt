package com.depromeet.baton.presentation.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
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

fun getHyphenPhone(phone : String) : String{
        var hypenedTel = phone.replace("-", "");
            if ( hypenedTel.length == 11) {
                // 010-1234-1234
                hypenedTel = hypenedTel.substring(0, 3) + "-" +  hypenedTel.substring(3, 7) + "-" +  hypenedTel.substring(7);
            } else if ( hypenedTel.length == 8) {
                // 1588-1234
                hypenedTel =  hypenedTel.substring(0, 4) + "-" +  hypenedTel.substring(4);
            } else {
                if ( hypenedTel.startsWith("02")) {
                    // 서울은 02-123-1234
                    hypenedTel = hypenedTel.substring(0, 2) + "-" +  hypenedTel.substring(2, 5) + "-" +  hypenedTel.substring(5);
                } else {
                    // 그외는 012-123-1345
                    hypenedTel =  hypenedTel.substring(0, 3) + "-" + hypenedTel.substring(3, 6) + "-" +  hypenedTel.substring(6);
                }
            }
        return  hypenedTel
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMsgDate(createdDate : String) : String{
    try{
        val pattern = "yyyy-MM-dd'T'HH:mm:ss"
        val fm = SimpleDateFormat(pattern)
        val formatDate = fm.parse(createdDate)
        val msgTime = Calendar.getInstance()
        if (formatDate != null) {
            msgTime.time = formatDate
        }
        msgTime.add(Calendar.HOUR_OF_DAY,9) //시차보정
        val currentTime = Calendar.getInstance()

        val diff = currentTime.timeInMillis - msgTime.timeInMillis
        if(((diff)/ 1000 / 60 /60 ) == 0L ){
            return "${((diff)/ 1000 /60 /60 /60 )}분 전"
        }
        else if(((diff)/ 1000 /60 /60 /24 ) == 0L ){
            return "${((diff)/ 1000 /60 /60 )}시간 전"
        }
        return "${((diff)/ 1000 / 60 /60 /24 )}일 전"

    }catch(e : Exception){
        Timber.e(e.message)
    }
    return ""
}