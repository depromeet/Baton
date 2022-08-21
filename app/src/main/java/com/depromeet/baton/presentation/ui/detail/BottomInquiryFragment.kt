package com.depromeet.baton.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomInQuiryBinding
import com.depromeet.baton.presentation.base.BaseBottomDialogFragment
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomInquiryFragment : BaseBottomDialogFragment<FragmentBottomInQuiryBinding>(R.layout.fragment_bottom_in_quiry) {
    private val ticketDetailViewModel: TicketDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ticketDetailViewModel = ticketDetailViewModel
        initView()
    }

    private fun initView() {
        binding.btnInquiry.setOnClickListener {
            ticketDetailViewModel.postInquiry(binding.etDescription.text.toString())
        }
    }
}