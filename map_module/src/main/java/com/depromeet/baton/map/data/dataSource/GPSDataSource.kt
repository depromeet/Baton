package com.depromeet.baton.map.data.dataSource

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import com.depromeet.baton.map.data.model.LocationModel
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class GPSDataSource @Inject constructor (applicationContext: Context){

    private val REQUEST_INTERVAL:Long = 1000

    private val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)  //자동으로 gps값을 받아온다.
    private var locationCallback: LocationCallback? =null
    private val geocoder = Geocoder(applicationContext)

    fun getLocation() : Flow<LocationModel> = callbackFlow {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for ((i, location) in result.locations.withIndex()) {
                    trySend(LocationModel(result.lastLocation))
                }
            }
        }
        setUpdateLocationListener()
        awaitClose {  stopLocationUpdates() }
    }

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //높은 정확도
            interval = REQUEST_INTERVAL //1초에 한번씩 GPS 요청
        }
        if(locationCallback != null)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            ).addOnFailureListener {
                Timber.e(it.message.toString())
            }

    }

     fun stopLocationUpdates(){
        if(locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback!!)
    }


}

