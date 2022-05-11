package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentMembershipInformationBinding
import com.depromeet.baton.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketInformationFragment : BaseFragment<FragmentMembershipInformationBinding>(R.layout.fragment_membership_information) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.membershipInformationFragment = this
        setTicketKindClickListener()
        setTermClickListener()
    }

    //판매회원권
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

    //TODO Choic Chip 디자인시스템에 없음  일단 button만들어서 기간제만 처리
    @SuppressLint("ResourceAsColor")
    private fun setTermClickListener() {
        with(binding) {
            bdschoiceMembershipFixedTerm.setOnClickListener {
                bdschoiceMembershipFixedTerm.setBackgroundResource(com.depromeet.bds.R.drawable.temp_choice_chip_background_selected)
                bdschoiceMembershipFixedTerm.setTextColor(Color.parseColor("#0066FF"))
            }
            etMembershipInfoPrice.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    etMembershipInfoPrice.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
                }
            }
            etMembershipInfoTerm.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    etMembershipInfoTerm.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
                } else {
                }
            }
        }
    }
}