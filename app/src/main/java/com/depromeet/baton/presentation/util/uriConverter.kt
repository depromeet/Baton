package com.depromeet.baton.presentation.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri

fun uriConverter(context: Context, id: Int): Uri {
    val resourceId = id
    val uriParser= Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(resourceId))
        .appendPath(context.resources.getResourceTypeName(resourceId))
        .appendPath(context.resources.getResourceEntryName(resourceId))
        .build()

    return uriParser
}
