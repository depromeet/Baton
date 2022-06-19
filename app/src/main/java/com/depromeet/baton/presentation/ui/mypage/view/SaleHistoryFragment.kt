package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleHistoryBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.adapter.MyPageViewAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SaleHistoryFragment  : BaseFragment<FragmentSaleHistoryBinding>(R.layout.fragment_sale_history){


    private lateinit var pagerAdapter: MyPageViewAdapter
    private val titles = listOf(
        "판매중",
        "거래내역"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


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
      runCatching{

            pagerAdapter = MyPageViewAdapter(childFragmentManager,this.lifecycle)
            binding.viewPager.adapter = pagerAdapter
            binding.viewPager.isSaveEnabled= false

            // TabLayout attach
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }.onFailure {
            Timber.e(it.message)
      }

    }

    override fun onDestroyView() {
        super.onDestroyView()
       // clearBackStack()
    }

   private fun clearBackStack() {
        val fragmentManager = childFragmentManager
        while (fragmentManager.backStackEntryCount !== 0) {
            fragmentManager.popBackStackImmediate()
        }
    }

}