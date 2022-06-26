package com.depromeet.baton.presentation.ui.address.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.depromeet.baton.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(val spfManager: BatonSpfManager): ViewModel(){

    private val _roadState = MutableLiveData<String>("도로명 주소")
    val roadState : LiveData<String> get()=_roadState

    private val _detailState = MutableLiveData<String>("")
    val detailState : LiveData<String> get()=_detailState

    //도보 시간
    private val _timeState = MutableLiveData<String>("걸어서 8분")
    val timeState: LiveData<String> get()=_timeState

    //설정 범위
    private val _maxDistance = MutableLiveData<String>("")
    val maxDistance : LiveData<String> get()=_maxDistance

    init {
        if(spfManager.getAddress().roadAddress !="" && spfManager.getAddress().address !=""){
            _roadState.value = spfManager.getAddress().roadAddress
            _detailState.value = "${spfManager.getDetailAddress()}"
        }

        if(spfManager.getMaxDistance().getMaxDistanceWithUnit()!="500m"){
            val distance =spfManager.getMaxDistance()!!
            _maxDistance.value = distance.getMaxDistanceWithUnit()
            _timeState.value =  "걸어서 ${getWalkingTime(distance.getMaxDistanceWithUnit())}분"
        }
    }


    fun distanceCalculator(value : Int, type : DistanceType) :Float{
        var resultMeter = 0.0F
        when(type){
            DistanceType.MAX500M ->{
                resultMeter = 500.0F
            }
            DistanceType.MAX1KM -> {
                resultMeter = value * 500 / 1000.0F + 500.0F
                val value =String.format("%1.0f",(resultMeter))
               _maxDistance.value=if(value == "500" ) "" else value +"m"
            }
           DistanceType.MAX3KM ->{
               resultMeter =  value%1000 *2 +1000.0F
                _maxDistance.value=  String.format("%1.1f",(resultMeter/1000.0)) +"km"
            }
           DistanceType.MAX5KM ->{
               resultMeter = (value % 2000 ) * 2 + 3000.0F
               _maxDistance.value=  String.format("%1.1f",(resultMeter/1000.0)) +"km"

            }
        }
        _timeState.value = "걸어서 ${getWalkingTime( maxDistance.value!!)}분"
        spfManager.saveMaxDistance(resultMeter.toInt())
        return resultMeter
    }


     fun setDistanceProgress(distance : MaxDistance) : Int{
        if(distance.unit=="km"){
            val value = distance.getDistanceToKM()
            if(value< 3.0){
                /*MAX3KM*/
                val process = value - 1.0 // MAK1KM
                return (process * 1000.0f /2 + 1000).toInt()
            }
            else{
                /*MAX5KM*/
                val process = value - 3.0 // MAK1KM
                return (process * 1000.0f / 2 + 2000).toInt()
            }

        }else{
            //m 단위
            /*MAX1KM*/
            val value = distance.getDistance()
            return (value-500)*1000 /500
        }
    }

    fun getWalkingTime(distance: String) : String{
        //사람 평균 4km
        val AVERAGE_SPEED =4
        return if(distance.contains("km")){
            val value = distance.substring(0,distance.indexOf("km")).toFloat()
            (value / AVERAGE_SPEED * 60).toInt().toString()

        }else{
            //m 단위
            if(distance=="") return  (500.0f/1000 /AVERAGE_SPEED * 60).toInt().toString()

            val value = distance.substring(0,distance.indexOf("m")).toFloat()
            (value/1000 /AVERAGE_SPEED * 60).toInt().toString()
        }
    }

}


enum class DistanceType{
    MAX500M, MAX1KM, MAX3KM, MAX5KM
}