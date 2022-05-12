package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.depromeet.baton.databinding.FragmentTransactionMethodBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.bds.R
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionMethodRegisterFragment : BaseFragment<FragmentTransactionMethodBinding>(com.depromeet.baton.R.layout.fragment_transaction_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionMethodRegisterFragment = this
        setOnClickListener()
        setToolTip()
    }

    //TODO 디자인 시스템에 없음, Bds Choice Chip으로 변경
    private fun setOnClickListener() {
        with(binding) {
            ctvTransactionMethodFace.setOnClickListener {
                ctvTransactionMethodFace.toggle()
            }
            ctvTransactionMethodNonFace.setOnClickListener {
                ctvTransactionMethodNonFace.toggle()
            }
            ctvTransactionMethodAll.setOnClickListener {
                ctvTransactionMethodAll.toggle()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setToolTip() {
        binding.tvTransactionMethodInfo.setOnClickListener {
            val balloon = Balloon.Builder(requireContext())
                .setWidthRatio(1.0f)
                .setHeight(BalloonSizeSpec.WRAP)
                .setMarginTop(5) //꼬리랑 마진
                .setMarginRight(12)  //마진
                .setMarginLeft(120)  //마진
                .setTextSize(15f)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowDrawableResource(R.drawable.ic_tooltip_subtract)
                .setArrowSize(10)
                .setArrowPosition(0.8f)
                .setPaddingLeft(10)
                .setPaddingTop(10)
                .setPaddingBottom(10)
                .setCornerRadius(4f)
                .setBackgroundColorResource(R.color.bg)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLayout(com.depromeet.baton.R.layout.tooltip)
                .setLifecycleOwner(requireActivity())
                .build()
            balloon.showAsDropDown(binding.tvTransactionMethodInfo, 0, 0) //밑에서 뜨게
        }
    }

    //TODO CheckBox Bds로 변경
}