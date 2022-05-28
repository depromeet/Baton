package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentProfileBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment(private val profileViewModel: ProfileViewModel) :BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileMyprofileIv.setOnClickListener {
            val bottomSheet = ProfileBottomFragment(profileViewModel)
            bottomSheet.show(requireActivity().supportFragmentManager, BatonApp.TAG)
        }
        setObserver()
    }


    private fun setObserver(){
        profileViewModel.isChangedImage.observe(viewLifecycleOwner){
            binding.profileMyprofileIv.setImageResource(profileViewModel.profileStatus.value!!.size56)
        }
    }

}