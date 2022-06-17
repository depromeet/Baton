package com.depromeet.baton.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment

fun Context?.showToast(message: CharSequence?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context?.showToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: CharSequence?) {
    requireContext().showToast(message)
}

fun Fragment.showToast(@StringRes resId: Int) {
    requireContext().showToast(resId)
}
