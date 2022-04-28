package com.depromeet.baton.presentation.ui.address

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddressBinding
import com.depromeet.baton.databinding.ActivityMylocationBinding
import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.UiState
import com.depromeet.baton.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MyLocationActivity :BaseActivity<ActivityMylocationBinding>(R.layout.activity_mylocation) {
    val mapViewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        fetchData()
    }

    fun  setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mapViewModel.uiState.collect {
                        when (it) {
                            is UiState.Loading -> {
                                binding.locationProgress.visibility= View.VISIBLE  //TODO : do something
                            }
                            is UiState.Success<AddressEntity> ->{
                                Timber.e("Loading")
                                showAddress(it.data)
                            }
                            is UiState.Error<AddressEntity> ->{

                            }
                        }
                    }
                }
            }
        }
    }

    private fun showAddress(data : AddressEntity){
        binding.locationProgress.visibility= View.GONE
        binding.addressDetailEt.visibility=View.VISIBLE
        binding.addressTv.text=data.address
        binding.roadAddressTv.text="[도로명 주소] : ${data.roadAddress}"

    }


    private fun fetchData() {
        mapViewModel.getMyAddress()
    }

}