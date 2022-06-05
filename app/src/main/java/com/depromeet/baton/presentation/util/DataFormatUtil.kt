package com.depromeet.baton.presentation.util

import java.text.DecimalFormat


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