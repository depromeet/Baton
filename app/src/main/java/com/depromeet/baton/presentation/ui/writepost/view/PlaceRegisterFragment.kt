package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPlaceRegisterBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel


class PlaceRegisterFragment : BaseFragment<FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment = this@PlaceRegisterFragment

        setSearchBar()
        selectedShopObserve()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setHashTagClickListener()
    }

    //SearchBar 초기 상태 세팅
    private fun setSearchBar() {
        with(binding.includeBdsSearchbarOne) {
            searchBarEt.text = Editable.Factory.getInstance().newEditable("헬스장 이름이나 도로명 주소를 검색해주세요")
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(Color.GRAY)
            searchBarCancelIc.visibility = View.GONE
        }
        with(binding.includeBdsSearchbarTwo) {
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(Color.GRAY)
            searchBarCancelIc.visibility = View.GONE
        }
    }

    //장소등록->바텀싯
    private fun setPlaceRegisterClickListener() {
        binding.includeBdsSearchbarOne.searchBarEt.setOnClickListener {
            val bottomFilterFragment = BottomSearchShopFragment()
            bottomFilterFragment.show(
                childFragmentManager,
                bottomFilterFragment.tag
            )
        }
    }

    //TODO textField Bds 적용
    private fun selectedShopObserve() {
        writePostViewModel.selectedShopInfo.observe(this) { selectedShopInfo ->
            binding.tvPlaceRegister.text = "이름"
            binding.tvPlaceRegisterExplain.text = "주소"

            with(binding.includeBdsSearchbarOne) {
                searchBarSearchIc.visibility = View.GONE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopName)
            }
            with(binding.includeBdsSearchbarTwo) {
                ctlSearchBarContainer.visibility = View.VISIBLE
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopAddress)
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarSearchIc.visibility = View.GONE

            }
        }
    }

    //TODO 사진 선택->갤러리
    private fun setPictureSelectClickListener() {

    }

    //해시태그 선택
    private fun setHashTagClickListener() {
        binding.includeChip.tvHashtagSelectExplain.text = "(선택사항)"

        with(binding.includeChip) {
            bdschoiceHashtagKindTeacher.setOnClickListener {
                bdschoiceHashtagKindTeacher.setOnAndShape(true, 0)
            }
            bdschoiceHashtagSystematicLesson.setOnClickListener {
                bdschoiceHashtagSystematicLesson.setOnAndShape(true, 0)
            }
            bdschoiceHashtagCustomizedCare.setOnClickListener {
                bdschoiceHashtagCustomizedCare.setOnAndShape(true, 0)
            }
            bdschoiceHashtagVariousInstruments.setOnClickListener {
                bdschoiceHashtagVariousInstruments.setOnAndShape(true, 0)
            }
            bdschoiceHashtagQuietAtmosphere.setOnClickListener {
                bdschoiceHashtagQuietAtmosphere.setOnAndShape(true, 0)
            }
            bdschoiceHashtagWideFacility.setOnClickListener {
                bdschoiceHashtagWideFacility.setOnAndShape(true, 0)
            }
            bdschoiceHashtagPleasantEnvironment.setOnClickListener {
                bdschoiceHashtagPleasantEnvironment.setOnAndShape(true, 0)
            }
        }
    }
}