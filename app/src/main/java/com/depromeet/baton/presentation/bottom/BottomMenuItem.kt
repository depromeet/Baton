package com.depromeet.baton.presentation.bottom


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomMenuItem(
    val listItem: String? = null,
    var isChecked: Boolean? = null
) :Parcelable


