package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPurchaseHistoryBinding

import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.ui.mypage.adapter.PurchaseTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.viewmodel.PurchaseHistoryViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
@AndroidEntryPoint
class PurchaseHistoryFragment: BaseFragment<FragmentPurchaseHistoryBinding>(R.layout.fragment_purchase_history){

    private val purchaseViewModel by viewModels<PurchaseHistoryViewModel>()

    private  val ticketItemRvAdapter by lazy {
        PurchaseTicketItemAdapter(requireContext(), ::onClickItemListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTicketItemRv()
        setOnBackPressed()
        setObserver()
    }



    private fun setTicketItemRv(){

        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.purchaseHistoryRv.adapter = ticketItemRvAdapter
        binding.purchaseHistoryRv.layoutManager = mLayoutManager
    }


    private fun setObserver() {

        purchaseViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach {
                binding.uistate = it
                ticketItemRvAdapter.submitList(it.list)
            }
            .launchIn(viewLifecycleScope)
    }


    //메뉴버튼 클릭

    private fun onClickItemListener(ticketItem : SaleTicketListItem){
        startActivity(TicketDetailActivity.start(requireContext(),ticketItem.ticket.typeId))
    }

    private fun setOnBackPressed(){
        binding.purchaseHistoryToolbar.setOnBackwardClick{parentFragmentManager.popBackStack()}
    }
}