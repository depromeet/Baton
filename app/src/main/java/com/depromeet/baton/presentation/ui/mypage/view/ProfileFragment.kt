package com.depromeet.baton.presentation.ui.mypage.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentProfileBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.MyPageViewModel
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment() :BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val profileViewModel by viewModels<ProfileViewModel>(ownerProducer = {requireActivity()})
    private val myPageViewModel by viewModels<MyPageViewModel>(ownerProducer = {requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setListener()
    }


    private fun initView(){
        val name = arguments?.getString("nickName" ,"")
        val phone =arguments?.getString("phoneNumber","")
        val profile = arguments?.getString("profileImg","")
        if(name!=null && phone!=null) profileViewModel.initProfileInfo(name, phone, profile!!)
    }

    private fun setListener(){
        binding.profileToolbar.setOnBackwardClick{ onBackPressed()}
        binding.profileCompleteBtn.setOnClickListener { setOnClickComplete() }

        binding.profileSettingBtn.setOnClickListener {
            val bottomSheet = ProfileBottomFragment()
            bottomSheet.show(this.childFragmentManager,"profileBottom")
        }
    }


    private fun changedProfile(){
        requireContext().BdsToast("변경이 완료됐습니다.",binding.profileCompleteBtn.top).show()
     //   binding.profileMyprofileIv.setImageURI(profileViewModel.temporaryUiState.value.profileImage)
        binding.profileCompleteBtn.isEnabled=true
    }


    private fun onBackPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun setOnClickComplete(){
        profileViewModel.submitProfile()
        onBackPressed()
    }

    private fun setObserver(){
        profileViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> run{
                binding.uiState = uiState
            } }
            .launchIn(viewLifecycleScope)

         profileViewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private fun handleViewEvents(profileViewEvents: List<ProfileViewModel.ProfileViewEvent>) {
        profileViewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ProfileViewModel.ProfileViewEvent.EventToBack -> {
                   onBackPressed()
                }
                ProfileViewModel.ProfileViewEvent.EventUpdateProfileImage->{
                    myPageViewModel.updateProfileImg(profileViewModel.uiState.value.profileImage)
                    changedProfile()
                }
                ProfileViewModel.ProfileViewEvent.EventUpdateProfileInfo ->{
                    myPageViewModel.updateNickname(profileViewModel.uiState.value.nickName)
                }
            }
            profileViewModel.consumeViewEvent(viewEvent)
        }
    }

}