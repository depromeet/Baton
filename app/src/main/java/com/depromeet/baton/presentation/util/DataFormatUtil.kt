package com.depromeet.baton.presentation.util

import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


fun priceFormat(price: Float): String {
    val priceStr = price.toInt().toString()
    val dec = DecimalFormat("###,###")

  /*  return if (priceStr.length >= 5)
        dec.format((priceStr.substring(0, priceStr.length - 4) + "0000").toInt())
    else "0"*/
    return dec.format(price)
}

fun priceFloatFormat(price: Float):String{
    val dec = DecimalFormat("###,###.##")
    return dec.format(price)
}

fun ceilAndToStringFormat(data: Float): String {
    return ceil(data).toInt().toString()
}

fun dateFormatUtil(date :String):String{
    try{
        val pattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val fm = SimpleDateFormat(pattern)
        fm.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        val formatDate= fm.parse(date)
        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
        val convertedStr= convertFormat.format(formatDate)
        return convertedStr
    }catch (e : Exception){
        val pattern = "yyyy-MM-dd'T'HH:mm:ss"
        val fm = SimpleDateFormat(pattern)
        fm.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        val formatDate= fm.parse(date)
        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
        val convertedStr= convertFormat.format(formatDate)
        return convertedStr
    }catch (e2:Exception){
        Timber.e(e2.message)
    }
    return date
}


fun distanceFormatUtil (distance: Double) :String{
    if(distance < 1000) return "${(distance).toInt()}m"
    else return "${(distance/1000).toInt()}km"
}