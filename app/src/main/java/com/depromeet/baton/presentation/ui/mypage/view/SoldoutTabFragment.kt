package com.depromeet.baton.presentation.ui.mypage.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSoldoutTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.mypage.adapter.SaleTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.ui.mypage.viewmodel.SaleHistoryViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SoldoutTabFragment  : BaseFragment<FragmentSoldoutTabBinding>(R.layout.fragment_soldout_tab) {

    private val saleViewModel by viewModels<SaleHistoryViewModel>(ownerProducer = {requireActivity()})
    private  val ticketItemRvAdapter by lazy {
        SaleTicketItemAdapter(requireContext(), ::onClickMenuItemListener,::onClickStatusMenuItemListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTicketItemRv()
        setObserver()
    }

    override fun onResume() {
        super.onResume()
        saleViewModel.getSoldoutHistory()
    }


    private fun setTicketItemRv(){
        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.saleTabRv.adapter = ticketItemRvAdapter
        binding.saleTabRv.layoutManager = mLayoutManager

    }


    private fun setObserver() {
        saleViewModel.soldoutUiState
            .flowWithLifecycle(viewLifecycle)
            .onEach {
                binding.uistate = it
                ticketItemRvAdapter.submitList(it.list)
            }
            .launchIn(viewLifecycleScope)
    }

    //메뉴버튼 클릭

    private fun onClickMenuItemListener(ticketItem : SaleTicketListItem, view: View){}

    //상태변경 클릭

    private fun onClickStatusMenuItemListener(ticketItem : SaleTicketListItem, pos:Int){
        //TODO 현재 ticket isChecked 처리
        showBottom(ticketItem,pos)
    }
    private fun showBottom(ticketItem: SaleTicketListItem,position: Int){
        val list = resources.getStringArray(R.array.ticketSaleStatus).map { BottomMenuItem(it)}
        list.get(2).isChecked=true
        val bottom = BottomSheetFragment.newInstance("상태 변경",list,
            BottomSheetFragment.CHECK_ITEM_VIEW, object: BottomSheetFragment.Companion.OnItemClick{
            override fun onSelectedItem(selected: BottomMenuItem, pos: Int) { //
                if(pos !=2 ){
                    saleViewModel.changeStatus(ticketItem.ticket.data.id, pos)
                }
            }}
        )
        bottom.show(childFragmentManager,null)
    }

}