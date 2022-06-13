package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentMembershipInformationBinding
import com.depromeet.baton.domain.model.AdditionalOptions
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.presentation.util.CustomTextWatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MembershipInformationFragment : BaseFragment<FragmentMembershipInformationBinding>(R.layout.fragment_membership_information) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        writePostViewModel.setNextLevelEnable()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.membershipInformationFragment = this
        binding.viewModel = writePostViewModel
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        writePostViewModel.setNextLevelEnable()
        setObserve()
        setEditText()
        setTermIsChecked()
        setCheckboxOnClickListener()

            binding.etTerm.addTextChangedListener(CustomTextWatcher(binding.etTerm))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserve() {
        writePostViewModel.membershipInfoUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
    }

    private fun setTermIsChecked() {
        writePostViewModel.isPeriodChecked.observe(viewLifecycleOwner) {
            binding.ctvMembershipFixedTerm.isChecked = it
        }
        writePostViewModel.isNumberChecked.observe(viewLifecycleOwner) {
            binding.ctvMembershipFrequencyTerm.isChecked = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCheckboxOnClickListener() {
        with(binding) {
            checkboxBargaining.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.BARGAINING, checkboxBargaining.isChecked)
            }
            checkboxShowerRoom.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.SHOWER_ROOM, checkboxShowerRoom.isChecked)
            }
            checkboxLockerRoom.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.LOCKER_ROOM, checkboxLockerRoom.isChecked)
            }
            checkboxSportWear.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.SPORT_WEAR, checkboxSportWear.isChecked)
            }
            checkboxGx.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.GX, checkboxGx.isChecked)
            }
            checkboxRefund.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.REFUND, checkboxRefund.isChecked)
            }
            checkboxRetransfer.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.RE_TRANSFER, checkboxRetransfer.isChecked)
            }
            checkboxHolding.setOnClickListener {
                writePostViewModel.handleChipChanged(AdditionalOptions.HOLDING, checkboxHolding.isChecked)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEditText() {
        with(binding) {
            etTerm.text = Editable.Factory.getInstance().newEditable(writePostViewModel.termWithDot.value)
            etTermNumber.text = Editable.Factory.getInstance().newEditable(writePostViewModel.termWithDot.value)
            etPrice.text = Editable.Factory.getInstance().newEditable(writePostViewModel.membershipInfoUiState.value.priceChanged)
        }
    }
}
