package com.depromeet.baton.presentation.util

import java.text.DecimalFormat
import kotlin.math.ceil


fun priceFormat(price: Float): String {
    val priceStr = price.toInt().toString()
    val dec = DecimalFormat("###,###")
    return dec.format(price)
}

fun priceFloatFormat(price: Float): String {
    val dec = DecimalFormat("###,###.##")
    return dec.format(price)
}

fun ceilAndToStringFormat(data: Float): String {
    return ceil(data).toInt().toString()
}