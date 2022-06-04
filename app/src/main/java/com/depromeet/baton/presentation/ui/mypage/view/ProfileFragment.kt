package com.depromeet.baton.presentation.ui.mypage.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentProfileBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.ui.sign.AddAccountActivity
import com.depromeet.baton.presentation.ui.sign.SignUpInfoViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment() :BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val profileViewModel : ProfileViewModel by lazy {
        ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileSettingBtn.setOnClickListener {
            val bottomSheet = ProfileBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, BatonApp.TAG)
        }
        setObserver()
        setListener()
        setBackPressed()

    }

    private fun setListener(){
        binding.profileToolbar.setOnBackwardClick{onBackPressed()}
        binding.profileCompleteBtn.setOnClickListener { setOnClickComplete() }
    }


    private fun changedProfile(){
        requireContext().BdsToast("변경이 완료됐습니다.",binding.profileCompleteBtn.top).show()
        binding.profileMyprofileIv.setImageURI(profileViewModel.temporaryUiState.value.profileImage)
        binding.profileCompleteBtn.isEnabled=true
    }

    private fun setBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 눌렀을 때 동작할 코드
                onBackPressed()
            }
        })
    }

    private fun onBackPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun setOnClickComplete(){
        onBackPressed()
    }

    private fun setObserver(){

        profileViewModel.temporaryUiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> binding.temporaryUiState = uiState }
            .launchIn(viewLifecycleScope)

        profileViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(viewLifecycleScope)

        profileViewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<ProfileViewModel.ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ProfileViewModel.ViewEvent.ToBack -> {
                   onBackPressed()
                }
                ProfileViewModel.ViewEvent.ToSettingProfileImg->{
                    changedProfile()
                }
            }
            profileViewModel.consumeViewEvent(viewEvent)
        }
    }

}