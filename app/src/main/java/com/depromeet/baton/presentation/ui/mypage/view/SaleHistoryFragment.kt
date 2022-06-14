package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleHistoryBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.adapter.MyPageViewAdapter
import com.depromeet.baton.presentation.ui.mypage.viewmodel.PurchaseHistoryViewModel
import com.depromeet.baton.presentation.ui.mypage.viewmodel.SaleHistoryViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SaleHistoryFragment  : BaseFragment<FragmentSaleHistoryBinding>(R.layout.fragment_sale_history){

    private val saleHistoryFragment = SaleTabFragment()
    private val soldOutTabFragment = SoldoutTabFragment()
    private val titles = listOf(
        "판매중",
        "거래내역"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        setListener()
    }

    private fun setListener(){
       binding.saleHistoryToolbar.setOnBackwardClick { onBackPressed()}
    }

    private fun onBackPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun initViewPager(){
        val pagerAdapter = MyPageViewAdapter(requireActivity())
        pagerAdapter.addFragment(saleHistoryFragment)
        pagerAdapter.addFragment(soldOutTabFragment)

        binding.viewPager.adapter = pagerAdapter

        // TabLayout attach
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }


}