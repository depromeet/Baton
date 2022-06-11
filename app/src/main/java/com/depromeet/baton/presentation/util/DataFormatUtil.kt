package com.depromeet.baton.presentation.util

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
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

fun ceilAndToStringFormat(data: Float): String {
    return ceil(data).toInt().toString()
}

fun dateWithDotFormat(input: String, editText: EditText) {
    val formattedText = when (input.length) {
        4 -> {
            "$input."
        }
        7 -> {
            "$input."
        }
        else -> return
    }
    editText.setText(formattedText)
    editText.setSelection(formattedText.length)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun dateDifferenceFormat(endDate: String): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    val startDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).replace("-", "")
    return (((dateFormat.parse(endDate)?.time ?: 0) - (dateFormat.parse(startDate)?.time ?: 0)) / (24 * 60 * 60 * 1000)).toString()
}