package com.depromeet.baton.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun RecyclerView.isScrollable(): Boolean {
    return canScrollVertically(1) || canScrollVertically(-1)
}

fun RecyclerView.setStackFromEnd() {
    (layoutManager as? LinearLayoutManager)?.stackFromEnd = true
}
