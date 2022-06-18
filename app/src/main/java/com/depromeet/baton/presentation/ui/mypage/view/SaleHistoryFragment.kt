package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleHistoryBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.adapter.MyPageViewAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SaleHistoryFragment  : BaseFragment<FragmentSaleHistoryBinding>(R.layout.fragment_sale_history){

    private val saleTabFragment = SaleTabFragment()
    private val soldOutTabFragment = SoldoutTabFragment()
    private lateinit var pagerAdapter: MyPageViewAdapter
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
        pagerAdapter = MyPageViewAdapter(this, arrayListOf(saleTabFragment,soldOutTabFragment))
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isSaveEnabled=false

        // TabLayout attach
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onDetach() {
        super.onDetach()
        clearBackStack()
    }

    private fun clearBackStack() {
        val fragmentManager = childFragmentManager
        while (fragmentManager.backStackEntryCount !== 0) {
            fragmentManager.popBackStackImmediate()
        }
    }

}