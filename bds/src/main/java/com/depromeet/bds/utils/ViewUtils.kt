package com.depromeet.bds.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
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

@ColorInt
internal fun Context.getAttributeColor(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return ContextCompat.getColor(this, typedValue.resourceId)
}

@StyleRes
internal fun Context.getAttributeStyle(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.resourceId
}
