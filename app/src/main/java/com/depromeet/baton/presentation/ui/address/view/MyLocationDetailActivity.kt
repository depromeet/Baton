package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import android.widget.Toast
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMylocationDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.util.getAddress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLocationDetailActivity : BaseActivity<ActivityMylocationDetailBinding>(R.layout.activity_mylocation_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setListener()
    }
    private fun initView(){
        binding.roadAddressTv.text= getAddress().roadAddress
        binding.addressTv.text = getAddress().address
        binding.detailAddressToolbar.titleTv.text ="상세 주소 입력"

    }

    private fun setListener(){
        binding.detailAddressToolbar.nextTv.setOnClickListener {
            Toast.makeText(this,"위치 정보가 저장되었습니다",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddressActivity::class.java)
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }


    }



}