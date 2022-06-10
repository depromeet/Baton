package com.depromeet.baton.presentation.ui.mypage.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSaleTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.ui.mypage.adapter.SaleTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.viewmodel.SaleHistoryViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SaleTabFragment : BaseFragment<FragmentSaleTabBinding>(R.layout.fragment_sale_tab){

    private val saleViewModel by viewModels<SaleHistoryViewModel>()
    private  val ticketItemRvAdapter by lazy {
        SaleTicketItemAdapter(requireContext(), ::onClickMenuItemListener, ::onClickStatusMenuItemListener)
    }

    private lateinit var alertDialog : AlertDialog


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
        showMenu(view, R.menu.menu_mypage_ticekt)
    }

    //상태변경 클릭

    private fun onClickStatusMenuItemListener(ticketItem : SaleTicketListItem){

        //TODO 현재 ticket isChecked 처리

    }

    //TODO menu Custom
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val wrapper = ContextThemeWrapper(requireContext(), com.depromeet.bds.R.style.BdsPopupMenuStyle)
        val popup = PopupMenu(wrapper, v,Gravity.END)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            onContextItemSelected(it)
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
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
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_mypage, null)
        alertDialog = AlertDialog.Builder(context, com.depromeet.bds.R.style.MyPageAlertDialog)
            .setView(view)
            .create()
        val buttonCancel = view.findViewById<Button>(R.id.dialog_cancel)
        val buttonConfirm = view.findViewById<Button>(R.id.dialog_delete)

        buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        buttonConfirm.setOnClickListener {
            //TODO 삭제 API
        }
    }

}