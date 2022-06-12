package com.depromeet.baton.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.naver.maps.geometry.LatLng
import timber.log.Timber

fun gpsConverter(context : Context, addressQuery : String) : LatLng {
    val coder = Geocoder(context)
   val result= runCatching {
        val address = coder.getFromLocationName(addressQuery, 1)
        if(address.isEmpty()) throw NullPointerException("좌표를 찾을 수 없습니다")
        val location: Address = address[0]
        LatLng(location.latitude, location.longitude)
    }.onFailure  {
        e-> Timber.e(e.message)
    }.getOrDefault(
        LatLng(0.0,0.0)
    )
    return result
}
