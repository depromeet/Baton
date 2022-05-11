package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.content.Intent.*
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.os.bundleOf
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMylocationDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.util.getAddress
import com.depromeet.baton.util.saveDetailAddress
import com.depromeet.bds.component.BdsSearchBar
import com.google.android.material.internal.ContextUtils.getActivity
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

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
        binding.detailAddressNextBtn.setOnClickListener {
            saveDetailAddress( binding.addressDetailEt.getText())
            Toast.makeText(this,"위치 정보가 저장되었습니다",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddressActivity::class.java)
            intent.putExtra("isChanged",true)
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        KeyboardVisibilityEvent.setEventListener(this) {
            binding.addressDetailEt.searchBarKeyBoardListener(it)
            binding.detailAddressNextBtn.isEnabled = !it
        }
    }

}