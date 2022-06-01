package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.content.Intent.*
import android.content.res.ColorStateList
import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMylocationDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.util.BatonSpfManager
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import javax.inject.Inject

@AndroidEntryPoint
class MyLocationDetailActivity : BaseActivity<ActivityMylocationDetailBinding>(R.layout.activity_mylocation_detail) {
    @Inject lateinit var spfManager: BatonSpfManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setListener()
    }
    private fun initView(){
        binding.roadAddressTv.text= spfManager.getAddress().roadAddress
        binding.addressTv.text = spfManager.getAddress().address
        binding.detailAddressToolbar.titleTv.text ="상세 주소 입력"
    }

    private fun setListener(){
        binding.detailAddressNextBtn.setOnClickListener {
            spfManager.saveDetailAddress( binding.addressDetailEt.getText())
            this.BdsToast("위치 정보가 저장되었습니다", binding.detailAddressNextBtn.top).show()
            val intent = Intent(this, AddressActivity::class.java)
            intent.putExtra("isChanged",true)
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.detailAddressToolbar.backBtn.setOnClickListener {
            onBackPressed()
        }

        KeyboardVisibilityEvent.setEventListener(this) {
            binding.addressDetailEt.searchBarKeyBoardListener(it)
            binding.detailAddressNextBtn.isEnabled = !it
        }
    }

}