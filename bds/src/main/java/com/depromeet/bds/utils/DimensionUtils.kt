package com.depromeet.bds.utils

import android.content.res.Resources

fun Float.toPx() = this * Resources.getSystem().displayMetrics.density
fun Float.toDp() = this / Resources.getSystem().displayMetrics.density

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()
