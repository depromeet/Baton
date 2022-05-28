package com.depromeet.baton.presentation.ui.mypage.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentProfileBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(private val profileViewModel: ProfileViewModel) :BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileSettingBtn.setOnClickListener {
            val bottomSheet = ProfileBottomFragment(profileViewModel,::changedProfile)
            bottomSheet.show(requireActivity().supportFragmentManager, BatonApp.TAG)
        }
        setObserver()
        setListener()
    }

    private fun setListener(){
        binding.profileToolbar.setOnBackwardClick{onBackPressed()}
        binding.profileCompleteBtn.setOnClickListener { setOnClickComplete() }
    }


    private fun changedProfile(){
        requireContext().BdsToast("변경이 완료됐습니다.",binding.profileCompleteBtn.top).show()
        binding.profileCompleteBtn.isEnabled=true
    }

    private fun onBackPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun setOnClickComplete(){
        onBackPressed()
    }


    private fun setObserver(){
        profileViewModel.isChangedImage.observe(viewLifecycleOwner){
            binding.profileMyprofileIv.setImageURI(profileViewModel.profileImageUri.value)
        }

    }


}