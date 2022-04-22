package com.depromeet.baton.map.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.map.BuildConfig
import com.depromeet.baton.map.databinding.ActivityMapBinding
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker

class MapActivity  : AppCompatActivity()  , OnMapReadyCallback {
    companion object{
        lateinit var naverMap: NaverMap
    }
    private val binding  : ActivityMapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }
  //  private val viewModel: SampleViewModel by viewModels()

    private val CLIENT_ID = BuildConfig.NAVER_API_CLIENT_ID_KEY
    private val SECRET_KEY = BuildConfig.NAVER_API_CLIENT_SECRET_KEY
    val _searchPos = MutableLiveData<Pos>(Pos(306985,545308))
    val searchPos : LiveData<Pos> get() = _searchPos
    lateinit var mapView: MapView
    val TAG="NAVER TEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        init()
    }

    override fun onMapReady(naverMap: NaverMap) {
        MapActivity .naverMap= naverMap
        val tm128= Tm128(searchPos.value?.xpos!!.toDouble(), searchPos.value?.ypos!!.toDouble())
        val latLng= tm128.toLatLng()
        var camPos = CameraPosition(
            latLng,
            13.0
        )
        val marker = Marker()
        marker.position = tm128.toLatLng()
        marker.map = Companion.naverMap


        naverMap.cameraPosition = camPos
    }

    fun init(){
        binding.searchBtn.setOnClickListener {
            val query = binding.searchEdittext.text.toString()
            getResultSearch(query)
        }
    }

    fun getResultSearch( query : String) {
//        val apiInterface: NaverApiInterface = ApiClient.instance!!.create(NaverApiInterface ::class.java)
//        apiInterface.getSearchResult(CLIENT_ID, SECRET_KEY, "local.json", query,5)?.enqueue(object :
//            Callback<NaverLocalResponse> {
//            override fun onResponse(call: Call<NaverLocalResponse>?, response: Response<NaverLocalResponse>) {
//                if (response.isSuccessful() && response.body() != null) {
//                    val result= response.body()!!
//                    Log.e(TAG, "성공 : $result")
//                    val pos = Pos(result.items.get(0).mapx, result.items.get(0).mapy)
//                    _searchPos.value =pos
//                    val tm128= Tm128(searchPos.value?.xpos!!.toDouble(), searchPos.value?.ypos!!.toDouble())
//
//                    Log.e(TAG, "pos : " + searchPos.value )
//                    var list = ""
//                    result.items.forEach {
//                            it -> list =  list.plus("[매장]:"+it.title+"/"+it.address+'\n')
//                    }
//                    Log.e(TAG, list)
//                    binding.resultTv.text=list
//
//                } else {
//                    Log.e(TAG, "실패 : " + response.body())
//                }
//            }
//            override fun onFailure(call: Call<NaverLocalResponse>?, t: Throwable) {
//                Log.e(TAG, "에러 : " + t.message)
//            }
//        })
    }
}
data class Pos (
    var xpos : Int=0,
    var ypos : Int =0
)