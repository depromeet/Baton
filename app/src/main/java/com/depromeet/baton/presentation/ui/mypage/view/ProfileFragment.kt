package com.depromeet.baton.presentation.ui.mypage.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentProfileBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
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
    private val myPageViewModel by viewModels<MyPageViewModel>(ownerProducer = {requireActivity()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setListener()
        profileViewModel.tokenError.observe(requireActivity()){
            (requireActivity() as MainActivity).showExpireToast()
        }
    }


    private fun initView(){
        profileViewModel.initProfileInfo()
    }

    private fun setListener(){
        binding.profileToolbar.setOnBackwardClick{ onBackPressed()}
        binding.profileCompleteBtn.setOnClickListener { setOnClickComplete() }

        binding.profileSettingBtn.setOnClickListener {
            val bottomSheet = ProfileBottomFragment()
            bottomSheet.show(this.childFragmentManager,"profileBottom")
        }
    }



    private fun onBackPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun setOnClickComplete(){
        profileViewModel.submitProfile()
    }

    private fun setObserver(){
        profileViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> run{
                binding.uiState = uiState
                Timber.d("hyomin ${uiState.profileImage}")
                Glide.with(requireContext())
                    .load(uiState.profileImage)
                    .error(com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_56)
                    .transform(CircleCrop())
                    .into(binding.profileMyprofileIv)
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
                    binding.profileCompleteBtn.isEnabled=true
                }
                ProfileViewModel.ProfileViewEvent.EventUpdateProfileInfo ->{
                    requireContext().BdsToast("변경이 완료됐어요.").show()
                    myPageViewModel.updateProfile(
                        profileViewModel.uiState.value.nickName,
                        profileViewModel.uiState.value.phoneNumber,
                        profileViewModel.uiState.value.profileImage.toString()
                        )
                    onBackPressed()
                }
            }
            profileViewModel.consumeViewEvent(viewEvent)
        }
    }

}