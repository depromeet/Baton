package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.ui.mypage.adapter.SaleTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.viewmodel.SaleHistoryViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.bds.component.BdsDialog
import com.depromeet.bds.component.DialogType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SaleTabFragment : BaseFragment<FragmentSaleTabBinding>(R.layout.fragment_sale_tab){

    private val saleViewModel by viewModels<SaleHistoryViewModel>(ownerProducer = {requireActivity()})
    private  val ticketItemRvAdapter by lazy {
        SaleTicketItemAdapter(requireContext(), ::onClickMenuItemListener, ::onClickStatusMenuItemListener)
    }

    private lateinit var alertDialog : BdsDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        setAlertDialog()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saleViewModel.getSaleHistory()
        setTicketItemRv()
        setObserver()
    }



    private fun setTicketItemRv(){
        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.saleTabRv.adapter = ticketItemRvAdapter
        binding.saleTabRv.layoutManager = mLayoutManager

    }


    private fun setObserver() {
        saleViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach {
                binding.uistate = it
                ticketItemRvAdapter.submitList(it.list)
            }
            .launchIn(viewLifecycleScope)
    }



    //메뉴버튼 클릭
    private fun onClickMenuItemListener(ticketItem : SaleTicketListItem, view: View){
        showMenu(view, R.menu.menu_mypage_ticekt ,ticketItem.ticket.data.id)
    }

    //상태변경 클릭
    private fun onClickStatusMenuItemListener(ticketItem : SaleTicketListItem,position:Int){
        //TODO 현재 ticket isChecked 처리
        showBottom(ticketItem,position)
    }


    private fun showBottom(ticketItem: SaleTicketListItem,position: Int){
        val list = resources.getStringArray(R.array.ticketSaleStatus).map { BottomMenuItem(it)}
        list.get(0).isChecked=true
        val bottom = BottomSheetFragment.newInstance("상태 변경",list,CHECK_ITEM_VIEW, object: BottomSheetFragment.Companion.OnItemClick{
            override fun onSelectedItem(selected: BottomMenuItem, pos: Int) { //
                if(pos !=0 ){
                    saleViewModel.changeStatus(ticketItem.ticket.data.id, pos)
                    ticketItemRvAdapter.removeSelectedItem(position)
                }
            }}
        )
        bottom.show(childFragmentManager,null)
    }


    private fun showMenu(v: View, @MenuRes menuRes: Int ,ticketId : Int) {
        val wrapper = ContextThemeWrapper(requireContext(), com.depromeet.bds.R.style.BdsPopupMenuStyle)
        val popup = PopupMenu(wrapper, v,Gravity.END)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            onContextItemSelected(it)
        }
        popup.show()
    }

    override fun onCreateOptionsMenu(menu : Menu,  inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mypage_ticekt, menu);
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mypage_ticket_menu_change-> {
                // Respond to context menu item 1 click.
                true
            }
            R.id.mypage_ticket_menu_delete -> {
                alertDialog.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun setAlertDialog(){
        alertDialog = BdsDialog(requireContext(), DialogType.SECONDARY)
        alertDialog.setHorizonDialog(::onClickConfirm , ::onClickCancel)
        alertDialog.setTitle("정말 삭제하시겠어요?")
        alertDialog.setContent("삭제시, 등록햇던 정보가 전부 사라져요")
        alertDialog.setImage(com.depromeet.bds.R.drawable.ic_img_empty_warning)
    }

    private fun onClickConfirm(){
        saleViewModel.deleteTicket(ticketItemRvAdapter.getSelectedItem().first!!.ticket.typeId)
        ticketItemRvAdapter.removeSelectedItem(ticketItemRvAdapter.getSelectedItem().second!!)
        alertDialog.dismiss()
    }
    private fun onClickCancel(){

    }

}