package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
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
                    binding.myLocationToolbar.nextTv.setTextColor(getColor(com.depromeet.bds.R.color.blue50))
                    saveAddress(locationViewModel.roadState.value!!, locationViewModel.jibunState.value!!)
                    binding.myLocationToolbar.nextTv.isEnabled=true
                }
                else ->{
                    binding.myLocationToolbar.nextTv.isEnabled=false
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
        binding.myLocationToolbar.nextTv.setOnClickListener {
            val intent = Intent(this, MyLocationDetailActivity::class.java)
            startActivity(intent)
        }
        binding.myLocationToolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }



}