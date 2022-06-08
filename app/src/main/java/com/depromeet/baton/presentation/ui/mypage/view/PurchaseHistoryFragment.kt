package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPurchaseHistoryBinding
import com.depromeet.baton.databinding.FragmentSaleTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.ui.mypage.adapter.PurchaseTicketItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
@AndroidEntryPoint
class PurchaseHistoryFragment: BaseFragment<FragmentPurchaseHistoryBinding>(R.layout.fragment_purchase_history){
    private  val ticketItemRvAdapter by lazy {
        PurchaseTicketItemAdapter(requireContext(), ::onClickItemListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTicketItemRv()
        setOnBackPressed()
    }



    private fun setTicketItemRv(){

        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.purchaseHistoryRv.adapter = ticketItemRvAdapter
        binding.purchaseHistoryRv.layoutManager = mLayoutManager

        //dummy
        val dummy = arrayListOf<SaleTicketItem>(
            SaleTicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy4,"2022.2.22","판매중"),
            SaleTicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3,"2022.2.22","판매중"),
            SaleTicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy2,"2022.2.22","판매중"),
            SaleTicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy1,"2022.2.22","판매중"),
            SaleTicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy5,"2022.2.22","판매중"),
        )
        val items : MutableList<SaleTicketListItem> = dummy.map{ i->
            SaleTicketListItem.PurchasedItem(i)
        }.toMutableList()
        items.add(SaleTicketListItem.Footer(dummy.last()))
        items.add(SaleTicketListItem.Header(dummy.last()))
        items.addAll(dummy.map{i-> SaleTicketListItem.PurchasedItem(i) })
        ticketItemRvAdapter.submitList(items)
    }


    private fun observeListItems() {
        /*  viewModel.ticketItem.observe(this) { items->
              ticketItemRvAdapter.submitList(items)
          }*/
    }


    //메뉴버튼 클릭

    private fun onClickItemListener(ticketItem : SaleTicketListItem){

    }

    private fun setOnBackPressed(){
        binding.purchaseHistoryToolbar.setOnBackwardClick{parentFragmentManager.popBackStack()}
    }
}