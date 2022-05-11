package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMylocationBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.ui.address.viewmodel.MyLocationViewModel
import com.depromeet.baton.util.saveAddress
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyLocationActivity :BaseActivity<ActivityMylocationBinding>(R.layout.activity_mylocation) {
    val locationViewModel by viewModels<MyLocationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = locationViewModel
        initView()
        setListener()
        setObserver()
        fetchData()

    }


    private fun initView(){
        binding.myLocationToolbar.titleTv.text="현재 위치"
    }

    private fun  setObserver() {
        locationViewModel.uiState.observe(this, Observer {
            when(it){
                is UIState.HasData->{
                    saveAddress(locationViewModel.roadState.value!!, locationViewModel.jibunState.value!!)
                    binding.myLocationDoneBtn.isEnabled=true

                }
                else ->{
                    binding.myLocationDoneBtn.isEnabled=false
                }
            }
        })

        locationViewModel.snackbarText.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Snackbar.make(binding.root, it,Snackbar.LENGTH_SHORT).show()
            }
        })

    }

    private fun fetchData() {
        locationViewModel.getMyAddress()
    }

    private fun setListener(){
        binding.myLocationDoneBtn.setOnClickListener {
            val intent = Intent(this, MyLocationDetailActivity::class.java)
            startActivity(intent)
        }
        binding.myLocationSearchBtn.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            startActivity(intent)
        }
    }



}