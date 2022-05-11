package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.depromeet.baton.databinding.FragmentTransactionMethodBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.bds.R
import com.depromeet.bds.utils.toPx
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect


class TransactionMethodRegisterFragment : BaseFragment<FragmentTransactionMethodBinding>(com.depromeet.baton.R.layout.fragment_transaction_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionMethodRegisterFragment = this
        setOnClickListener()
        setToolTip()
    }

    //TODO Choice Chip
    private fun setOnClickListener() {
        with(binding) {
            btnTransactionMethodFace.setOnClickListener {
                btnTransactionMethodFace.setBackgroundResource(com.depromeet.bds.R.drawable.temp_choice_chip_background_selected)
                btnTransactionMethodFace.setTextColor(Color.parseColor("#0066FF"))
            }
            btnTransactionMethodNonFace.setOnClickListener {
                btnTransactionMethodNonFace.setBackgroundResource(com.depromeet.bds.R.drawable.temp_choice_chip_background_selected)
                btnTransactionMethodNonFace.setTextColor(Color.parseColor("#0066FF"))
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
                .setMarginRight(8)  //마진
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
                .setBackgroundColorResource(R.color.bg10)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLayout(com.depromeet.baton.R.layout.tooltip)
                .setLifecycleOwner(requireActivity())
                .build()
            balloon.showAsDropDown(binding.tvTransactionMethodInfo, 0, 0) //밑에서 뜨게
        }
    }
}