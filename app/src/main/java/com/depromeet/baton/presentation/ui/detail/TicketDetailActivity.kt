package com.depromeet.baton.presentation.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.bottom.CheckItem
import com.depromeet.baton.presentation.ui.detail.model.TicketOwner
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class TicketDetailActivity :BaseActivity<ActivityTicketDetailBinding>(R.layout.activity_ticket_detail) {
    private val user = TicketOwner.SELLER
    private val viewModel by viewModels<TicketDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        initView()
        setObserver()

    }

    fun setObserver(){
        viewModel.uiState.observe(this, Observer{
            when(it){
                is UIState.Loading ->{

                }
                is UIState.HasData ->{
                    binding.ticketDetailToolbar.ticketToolbarTv.text= viewModel.marketInfoState.value?.gymName
                  // createHashTag()
                }
            }
        })
    }

    fun initView(){
        binding.ticketDetailRv.apply {
            val mLayoutManager = LinearLayoutManager(this@TicketDetailActivity)
            mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            this.layoutManager=mLayoutManager
        }

        binding.ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {

        }

        when(user){
            TicketOwner.SELLER ->{
                initSellerBottom()
            }
            TicketOwner.BUYER->{
                initBuyerBottom()
            }
        }
    }

    fun initSellerBottom(){
        val menuList = resources.getStringArray(R.array.ticket_detail_bottomsheet_menu)
        val bottomMenu : MutableList<CheckItem<String>> = menuList.map{it -> CheckItem(it)}.toMutableList()
        binding.ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {
            val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(
                "글 메뉴", bottomMenu,
                BottomSheetFragment.DEFAULT_ITEM_VIEW
            ) {
                when (it) {
                    Seller_TicketDetailMenu.CHANGE_SALES_OPTION.ordinal -> {
                        showChangeSalesOptionDialog()

                    }
                    Seller_TicketDetailMenu.EDIT_OPTION.ordinal -> {

                    }
                    Seller_TicketDetailMenu.DELETE_OPTION.ordinal -> {
                    }
                }
            }
            bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)
        }
    }

    fun initBuyerBottom(){

    }


    fun showChangeSalesOptionDialog(){
        val menuList = resources.getStringArray(R.array.change_sales_status_bottomsheet_menu)
            .map{it->CheckItem(it,false)}.toMutableList()

        val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment("상태 변경", menuList ,
            CHECK_ITEM_VIEW
        ) {
            when(it){
                //TODO 판매중/ 예약중/ 거래완료 분기처리
                Seller_TicketDetailStatusMenu.SALE.ordinal->{
                    binding.ticketDetailStatusReserve.visibility= View.GONE
                    binding.ticketDetailStatusSoldout.visibility= View.GONE
                }
                Seller_TicketDetailStatusMenu.RESERVATON.ordinal->{
                    binding.ticketDetailStatusReserve.visibility= View.VISIBLE
                    binding.ticketDetailStatusSoldout.visibility= View.GONE
                }
                Seller_TicketDetailStatusMenu.SOLDOUT.ordinal->{
                    binding.ticketDetailStatusSoldout.visibility= View.VISIBLE
                    binding.ticketDetailStatusReserve.visibility= View.GONE
                }
            }
        }
        bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)
    }


    enum class Seller_TicketDetailMenu{
        CHANGE_SALES_OPTION, EDIT_OPTION, DELETE_OPTION
    }

    enum class Seller_TicketDetailStatusMenu{
        SALE, RESERVATON, SOLDOUT
    }

  /*  fun createHashTag(){
        val vi = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = vi.inflate(com.depromeet.bds.R.layout.bds_component_tag, null)

        val tv = v?.findViewById<TextView>(com.depromeet.bds.R.id.tv_text)
        tv.text= "sample"

        val insertPoint = findViewById<View>(R.id.ticket_detail_tag_wrapper) as ViewGroup
        insertPoint.addView(v, ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
*/
}

