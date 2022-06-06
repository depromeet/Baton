package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentMembershipInformationBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MembershipformationFragment : BaseFragment<FragmentMembershipInformationBinding>(R.layout.fragment_membership_information) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        writePostViewModel.setNextLevelEnable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.membershipInformationFragment = this
        setTicketKindClickListener()
        setTermClickListener()
    }

    //TODO Binding Adapter로 연결 판매회원권
    private fun setTicketKindClickListener() {
        with(binding) {
            bdschoiceMembershipInfoGym.setOnClickListener {
                bdschoiceMembershipInfoGym.setOnAndShape(true, 0)
            }
            bdschoiceMembershipInfoPt.setOnClickListener {
                bdschoiceMembershipInfoPt.setOnAndShape(true, 0)
            }
            bdschoiceMembershipInfoPilates.setOnClickListener {
                bdschoiceMembershipInfoPilates.setOnAndShape(true, 0)
            }
            bdschoiceTicketKindEtc.setOnClickListener {
                bdschoiceTicketKindEtc.setOnAndShape(true, 0)
            }
        }
    }

    //TODO 디자인 시스템에 없는 Choice Chip, CheckedTextView로 사용중/ textfield Bds로 변경
    private fun setTermClickListener() {
        with(binding) {
            ctvMembershipFixedTerm.setOnClickListener {
                ctvMembershipFixedTerm.toggle()
            }
            ctvMembershipFrequencyTerm.setOnClickListener {
                ctvMembershipFrequencyTerm.toggle()
            }
            etMembershipInfoTerm.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    etMembershipInfoTerm.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
                }
            }
            etMembershipInfoPrice.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    etMembershipInfoPrice.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
                }
            }
        }

        //TODO CheckBox Bds로 변경
    }
}