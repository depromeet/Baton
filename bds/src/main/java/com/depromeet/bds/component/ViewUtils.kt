package com.depromeet.bds.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat

internal fun TypedArray.getResourceIdOrNull(@StyleableRes index: Int): Int? {
    return getResourceId(
        index,
        -1
    ).takeIf { it != -1 }
}

internal fun Int?.toDrawable(context: Context): Drawable? {
    return this?.let { ContextCompat.getDrawable(context, it) }
}

internal fun TextView.setTextAppearanceCompat(resId: Int) {
    TextViewCompat.setTextAppearance(this, resId)
}
