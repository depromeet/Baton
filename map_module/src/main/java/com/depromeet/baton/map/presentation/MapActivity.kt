package com.depromeet.baton.map.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.map.databinding.ActivityMapBinding
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.UiState
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapActivity  : AppCompatActivity() {
    companion object {
        val PERMISSION_REQUEST = 99
        lateinit var naverMap: NaverMap
        var permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        //권한 가져오기
    }

    val mapViewModel by viewModels<MapViewModel>()

    private val binding: ActivityMapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()

        if (isPermitted()) fetchData()
        else requestPermissions(permissions, PERMISSION_REQUEST)//권한 확인


    }


    private fun isPermitted(): Boolean {
        for (perm in permissions)
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) return false
        return true
    }//권한을 허락 받아야함

    fun  setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mapViewModel.uiState.collect {
                        when (it) {
                            UiState.Loading -> {
                                //TODO : do something
                            }
                        }
                    }
                }
            }
        }

        mapViewModel.address.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {

                }
                is NetworkResult.Error -> {
                    // show error message
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }


    private fun fetchData() {
        mapViewModel.getMyAddress()
    }

}
