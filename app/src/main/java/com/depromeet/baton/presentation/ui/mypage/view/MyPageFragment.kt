package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {


    private val profileViewModel by viewModels<ProfileViewModel>()

    private val saleHistoryFragment = SaleHistoryFragment()
    private val purchaseHistoryFragment = PurchaseHistoryFragment()
    private val likeTicketFragment = LikeTicketFragment()
    private val profileEditFragment by lazy{ ProfileFragment(profileViewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    private fun initView(){
        with(binding){
            mypageSaleHistoryCd.setOnClickListener {
                replaceFragment(saleHistoryFragment)
            }
            mypagePurchaseCd.setOnClickListener {
                replaceFragment(purchaseHistoryFragment)
            }
            mypageLikeCd.setOnClickListener {
                replaceFragment(likeTicketFragment)
            }
            mypageProfileIv.setOnClickListener {
                replaceFragment(profileEditFragment)
            }
        }
    }

    private fun setObserver(){
        profileViewModel.profileImageUri.observe(viewLifecycleOwner){
            binding.mypageProfileIv.setImageURI(it)
        }
    }

    private fun replaceFragment(fragment: Fragment){
        childFragmentManager.beginTransaction().add(R.id.fragment_container_view,fragment).addToBackStack(null).commit()
    }
}