package com.depromeet.baton.presentation.ui.mypage.view

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

    private val profileViewModel by viewModels<ProfileViewModel>(ownerProducer = {requireParentFragment()})
    private val myPageViewModel by viewModels<MyPageViewModel>(ownerProducer = {requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setListener()
        setBackPressed()

    }


    private fun initView(){
        val name = myPageViewModel.uiState.value.nickName
        val phone = myPageViewModel.uiState.value.phoneNumber
        val profile = myPageViewModel.uiState.value.profileImage.toString()
         if(name!=null && phone!=null) profileViewModel.initProfileInfo(name, phone, profile)


    }

    private fun setListener(){
        binding.profileToolbar.setOnBackwardClick{ onBackPressed()}
        binding.profileCompleteBtn.setOnClickListener { setOnClickComplete() }

        binding.profileSettingBtn.setOnClickListener {
            val bottomSheet = ProfileBottomFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, BatonApp.TAG)
        }
    }


    private fun changedProfile(){
        requireContext().BdsToast("변경이 완료됐습니다.",binding.profileCompleteBtn.top).show()
     //   binding.profileMyprofileIv.setImageURI(profileViewModel.temporaryUiState.value.profileImage)
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

    companion object{
        fun newInstance(nickname : String, phoneNumber : String , profile : String ):ProfileFragment {
            val args = Bundle().apply {
                putString("nickName", nickname)
                putString("phoneNumber", phoneNumber)
                putString("profileImg", profile)
            }
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

}