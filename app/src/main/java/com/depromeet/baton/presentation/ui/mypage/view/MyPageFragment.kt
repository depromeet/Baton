package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val saleHistoryFragment = SaleHistoryFragment()
    private val purchaseHistoryFragment = PurchaseHistoryFragment()
    private val likeTicketFragment = LikeTicketFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView(){
        with(binding){
            mypageSaleHistoryCd.setOnClickListener {
                childFragmentManager.beginTransaction().add(R.id.fragment_container_view,saleHistoryFragment).addToBackStack(null).commit()
            }
            mypagePurchaseCd.setOnClickListener {
                childFragmentManager.beginTransaction().add(R.id.fragment_container_view,purchaseHistoryFragment).addToBackStack(null).commit()
            }
            mypageLikeCd.setOnClickListener {
                childFragmentManager.beginTransaction().add(R.id.fragment_container_view,likeTicketFragment).addToBackStack(null).commit()
            }
        }
    }
}