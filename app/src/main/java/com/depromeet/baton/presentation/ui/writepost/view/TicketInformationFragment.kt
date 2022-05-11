package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentMembershipInformationBinding
import com.depromeet.baton.presentation.base.BaseFragment

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
    //판매회원권
    private fun setTermClickListener() {
        with(binding) {
            bdschoiceMembershipFixedTerm.setOnClickListener {
                bdschoiceMembershipFixedTerm.setOnAndShape(true, 0)
            }
            bdschoiceMembershipFrequencyTerm.setOnClickListener {
                bdschoiceMembershipFrequencyTerm.setOnAndShape(true, 0)
            }
        }
    }
}