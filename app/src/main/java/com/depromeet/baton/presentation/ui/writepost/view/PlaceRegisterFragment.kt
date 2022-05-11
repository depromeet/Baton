package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPlaceRegisterBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.view.BottomFilterFragment


class PlaceRegisterFragment : BaseFragment<FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment = this@PlaceRegisterFragment
        binding.includeChip.tvHashtagSelectExplain.text = "(선택사항)"
        setSearchBar()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setHashTagClickListener()
    }

    private fun setSearchBar() {
        with(binding) {
            includeBdsSearchbarOne.searchBarEt.text = Editable.Factory.getInstance().newEditable("헬스장 이름이나 도로명 주소를 검색해주세요")
            includeBdsSearchbarOne.searchBarEt.isFocusable = false
            includeBdsSearchbarOne.searchBarEt.setTextColor(Color.GRAY)
            includeBdsSearchbarOne.searchBarCancelIc.visibility=View.GONE
        }
    }

    //TODO 장소등록->바텀싯   ->콜백받아서  이름/주소로 바뀌고 서치바 하나 더생김
    private fun setPlaceRegisterClickListener() {
        binding.includeBdsSearchbarOne.ctlSearchBarContainer.setOnClickListener {
            val bottomFilterFragment = BottomFilterFragment()
            bottomFilterFragment.show(
                childFragmentManager,
                bottomFilterFragment.tag
            )
        }
        binding.includeBdsSearchbarOne.searchBarEt.setOnClickListener{
            val bottomFilterFragment = BottomFilterFragment()
            bottomFilterFragment.show(
                childFragmentManager,
                bottomFilterFragment.tag
            )
        }
    }

    //TODO 사진 선택->갤러리
    private fun setPictureSelectClickListener() {

    }

    //해시태그 선택
    private fun setHashTagClickListener() {
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
        }
    }
}