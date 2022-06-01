package com.depromeet.baton.presentation.ui.mypage.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSoldoutTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.mypage.adapter.SaleTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.adapter.SoldoutTicketItemAdapter
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem

class SoldoutTabFragment  : BaseFragment<FragmentSoldoutTabBinding>(R.layout.fragment_soldout_tab) {
    private  val ticketItemRvAdapter by lazy {
        SoldoutTicketItemAdapter(requireContext(), ::onClickStatusMenuItemListener)
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
    }



    private fun setTicketItemRv(){

        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.saleTabRv.adapter = ticketItemRvAdapter
        binding.saleTabRv.layoutManager = mLayoutManager

        //dummy
        val dummy = arrayListOf<SaleTicketItem>(
            SaleTicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy4,"2022.2.22","판매중"),
            SaleTicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3,"2022.2.22","판매중"),
            SaleTicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy2,"2022.2.22","판매중"),
            SaleTicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy1,"2022.2.22","판매중"),
            SaleTicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy5,"2022.2.22","판매중"),
        )
        val items : MutableList<SaleTicketListItem> = dummy.map{ i-> SaleTicketListItem.Item(i) }.toMutableList()
        items.add(SaleTicketListItem.Footer(dummy.last()))
        items.add(SaleTicketListItem.Header(dummy.last()))
        items.addAll(dummy.map{i-> SaleTicketListItem.Item(i) })
        ticketItemRvAdapter.submitList(items)

    }


    private fun observeListItems() {
        /*  viewModel.ticketItem.observe(this) { items->
              ticketItemRvAdapter.submitList(items)
          }*/
    }


    //메뉴버튼 클릭

    private fun onClickMenuItemListener(ticketItem : SaleTicketListItem, view: View){
        showMenu(view, R.menu.menu_mypage_ticekt)
    }

    //상태변경 클릭

    private fun onClickStatusMenuItemListener(ticketItem : SaleTicketListItem){

        //TODO 현재 ticket isChecked 처리
        val menuList = resources.getStringArray(R.array.ticketSaleStatus)
            .map { it -> BottomMenuItem(it, false) }.toMutableList()

        val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(
            "상태 변경", menuList,
            BottomSheetFragment.CHECK_ITEM_VIEW
        ) {
            when (it) {

            }
        }
        bottomSheetFragment.show(requireActivity().supportFragmentManager, BatonApp.TAG)
    }

    //TODO menu Custom
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val wrapper = ContextThemeWrapper(requireContext(), com.depromeet.bds.R.style.BdsPopupMenuStyle)
        val popup = PopupMenu(wrapper, v, Gravity.END)
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

    override fun onCreateOptionsMenu(menu : Menu, inflater: MenuInflater) {
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