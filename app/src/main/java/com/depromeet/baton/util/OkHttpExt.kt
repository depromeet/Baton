package com.depromeet.baton.util

import okhttp3.Request

fun Request.Builder.addHeaderIfPresent(name: String, value: String?) = apply {
    if (value != null) addHeader(name, value)
}
