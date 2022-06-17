package com.depromeet.baton.presentation.ui.sign

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressData(
    val roadAddress: String,
    val address: String,
    val latitude: Float,
    val longitude: Float,
) : Parcelable
