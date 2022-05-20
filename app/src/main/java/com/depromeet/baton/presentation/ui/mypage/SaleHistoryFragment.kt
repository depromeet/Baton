package com.depromeet.baton.presentation.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleHistoryBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class SaleHistoryFragment  : BaseFragment<FragmentSaleHistoryBinding>(R.layout.fragment_sale_history){
    private val saleHistoryFragment = SaleTabFragment()
    private val soldOutTabFragment = SoldoutTabFragment()
    private val titles = listOf(
        "판매중",
        "거래내역"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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