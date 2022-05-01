package com.depromeet.baton.util

import android.graphics.Rect
import android.view.View

val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD =128

fun isKeyboardShown(rootView: View) :Boolean{
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    val dm = rootView.resources.displayMetrics
    val heightDiff = rootView.bottom - r.bottom
    return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density
}


