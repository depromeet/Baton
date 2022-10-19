package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.databinding.FragmentTransactionMethodRegisterBinding
import com.depromeet.baton.domain.model.TransferFee
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.bds.R
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TransactionMethodRegisterFragment : BaseFragment<FragmentTransactionMethodRegisterBinding>(com.depromeet.baton.R.layout.fragment_transaction_method_register) {

    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        writePostViewModel.setNextLevelEnable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionMethodRegisterFragment = this
        binding.viewModel = writePostViewModel
        initView()
    }

    private fun initView() {
        setObserve()
        setTermIsChecked()
        setCheckboxOnClickListener()
    }

    private fun setObserve() {
        writePostViewModel.transactionMethodUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)

        writePostViewModel.transactionMethodViewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<WritePostViewModel.TransactionMethodViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                WritePostViewModel.TransactionMethodViewEvent.ShowToolTip -> {
                    showToolTip()
                }
            }
            writePostViewModel.transactionMethodConsumeViewEvent(viewEvent)
        }
    }

    private fun setTermIsChecked() {
        writePostViewModel.isFaceChecked.observe(viewLifecycleOwner) {
            binding.ctvTransactionMethodFace.isChecked = it
        }
        writePostViewModel.isNonFaceChecked.observe(viewLifecycleOwner) {
            binding.ctvTransactionMethodNonFace.isChecked = it
        }
        writePostViewModel.isBothChecked.observe(viewLifecycleOwner) {
            binding.ctvTransactionMethodAll.isChecked = it
        }
    }

    @SuppressLint("ResourceType")
    private fun showToolTip() {
        val balloon = Balloon.Builder(requireContext())
            .setHeight(BalloonSizeSpec.WRAP)
            .setWidth(BalloonSizeSpec.WRAP)
            .setMarginTop(5)
            .setMarginRight(12)
            .setTextSize(15f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowDrawableResource(R.drawable.ic_tooltip_subtract)
            .setArrowSize(10)
            .setArrowPosition(0.8f)
            .setPaddingLeft(10)
            .setPaddingRight(10)
            .setPaddingTop(10)
            .setPaddingBottom(10)
            .setCornerRadius(4f)
            .setBackgroundColorResource(R.color.bg)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLayout(com.depromeet.baton.R.layout.tooltip)
            .setLifecycleOwner(requireActivity())
            .build()
        balloon.showAsDropDown(binding.tvTransactionMethodInfo, 0, 0)
    }

    private fun setCheckboxOnClickListener() {
        with(binding) {
            checkboxSeller.setOnClickListener {
                writePostViewModel.handleChipChanged(TransferFee.SELLER, checkboxSeller.isChecked)
            }
            checkboxConsumer.setOnClickListener {
                writePostViewModel.handleChipChanged(TransferFee.CONSUMER, checkboxConsumer.isChecked)
            }
            checkboxNone.setOnClickListener {
                writePostViewModel.handleChipChanged(TransferFee.NONE, checkboxNone.isChecked)
            }
        }
    }
}