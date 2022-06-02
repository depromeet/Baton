package com.depromeet.baton.presentation.ui.address.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import com.depromeet.baton.util.BatonSpfManager
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyLocationActivity :BaseActivity<ActivityMylocationBinding>(R.layout.activity_mylocation) {
    val locationViewModel by viewModels<MyLocationViewModel>()

    @Inject
    lateinit var spfManager : BatonSpfManager


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
        binding.myLocationProgress.setAnimation("spinner.json")

    }

    private fun  setObserver() {
        locationViewModel.uiState.observe(this, Observer {
            when(it){
                is UIState.HasData->{
                    setToolTip()

                    spfManager.saveAddress(locationViewModel.roadState.value!!, locationViewModel.jibunState.value!!)
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
        binding.myLocationToolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setToolTip() {
        val balloon = Balloon.Builder(this)
            .setWidthRatio(0.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setElevation(3)
            .setMarginBottom(5)
            .setMarginLeft(16)
            .setTextSize(10f)
            .setTextColor(getColor(com.depromeet.bds.R.color.gy90))
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowDrawableResource(com.depromeet.bds.R.drawable.ic_tooltip_subtract)
            .setArrowSize(10)
            .setArrowPosition(0.0f)
            .setPadding(10)
            .setCornerRadius(4f)
            .setBackgroundColorResource(com.depromeet.bds.R.color.bg)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setText("현재 위치가 아니신가요?")
            .setLifecycleOwner(this)
            .build()
       binding.myLocationSearchBtn.showAlignTop(balloon,-100,0)
    }

}