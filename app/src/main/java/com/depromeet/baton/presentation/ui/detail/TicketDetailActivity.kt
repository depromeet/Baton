package com.depromeet.baton.presentation.ui.detail

import android.content.Intent
import android.icu.lang.UCharacter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.domain.model.TicketStatus
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.bottom.MenuItem
import com.depromeet.baton.presentation.ui.detail.model.TicketOwner
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.baton.presentation.util.TicketIteHorizontalDecoration
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TicketDetailActivity : BaseActivity<ActivityTicketDetailBinding>(R.layout.activity_ticket_detail) {
    private val user = TicketOwner.SELLER  //임시 데이터
    private var ticketStatus = TicketStatus.SALE

    private val viewModel by viewModels<TicketDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        initView()
        setListener()
        setObserver()
    }


    private fun initView() {
        with(binding) {
            val ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_HORIZONTAL, this@TicketDetailActivity, ::setTicketItemClickListener)
            val mLayoutManager = LinearLayoutManager(this@TicketDetailActivity, LinearLayoutManager.HORIZONTAL, false)

            ticketDetailRv.addItemDecoration(TicketIteHorizontalDecoration())
            ticketDetailRv.adapter = ticketItemRvAdapter
            ticketDetailRv.layoutManager = mLayoutManager

            ticketItemRvAdapter.submitList(
                arrayListOf(
                    TicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy4),
                    TicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy2),
                    TicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy1),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy5),
                )
            )
        }

        //TODO : 판매자 & 구매자에 따라 화면 초기화 나누기
        when (user) {
            TicketOwner.SELLER -> {
                initSellerBottom()
            }
            TicketOwner.BUYER -> {
                initBuyerBottom()
            }
        }

        //TODO : 티켓 상태 받아와서 초기화하기
        when (ticketStatus) {
            TicketStatus.SALE -> setSales()
            TicketStatus.RESERVATION -> setReservation()
            TicketStatus.SOLDOUT -> setSoldOut()
        }
    }


    private fun setLikeBtnClickListener(view: CheckedTextView) {
        view.setOnClickListener {
            view.toggle()
        }
    }


    private fun setObserver() {
        viewModel.uiState.observe(this, Observer {
            when (it) {
                is UIState.Loading -> {

                }
                is UIState.HasData -> {
                    binding.ticketDetailToolbar.ticketToolbarTv.text = viewModel.marketInfoState.value?.gymName
                    // createHashTag()
                }
            }
        })
    }


    private fun setListener() {


        val menuList = resources.getStringArray(R.array.ticket_detail_bottomsheet_menu)
        val bottomMenu: MutableList<BottomMenuItem<String>> = menuList.map { it -> BottomMenuItem(it) }.toMutableList()
        with(binding) {
            //메뉴버튼 bottomsheet
            ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {
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

            ticketDetailToolbar.ticketToolbarBackIc.setOnClickListener {
                onBackPressed()
            }
            //좋아요 toggle
            setLikeBtnClickListener(ticketDetailLikeBtn)

            ticketDetailUrlBtn.setOnClickListener {
                val url = "http://naver.me/5dxygLoW"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        }
    }


    fun initSellerBottom() {
        val menuList = resources.getStringArray(R.array.ticket_detail_bottomsheet_menu)
        val bottomMenu: MutableList<BottomMenuItem<String>> = menuList.map { it -> BottomMenuItem(it) }.toMutableList()
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

    fun initBuyerBottom() {

    }


    fun showChangeSalesOptionDialog() {
        val menuList = resources.getStringArray(R.array.ticketSaleStatus)
            .map { it -> BottomMenuItem(it, false) }.toMutableList()

        val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(
            "상태 변경", menuList,
            CHECK_ITEM_VIEW
        ) {
            when (it) {
                //TODO 판매중/ 예약중/ 거래완료 분기처리
                TicketStatus.SALE.ordinal -> {
                    setSales()
                }
                TicketStatus.RESERVATION.ordinal -> {
                    setReservation()
                }
                TicketStatus.SOLDOUT.ordinal -> {
                    setSoldOut()
                }
            }
        }
        bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)
    }

    fun setSoldOut() {
        binding.ticketDetailStatusSoldout.visibility = View.VISIBLE
        binding.ticketDetailStatusReserve.visibility = View.GONE
    }

    fun setReservation() {
        binding.ticketDetailStatusReserve.visibility = View.VISIBLE
        binding.ticketDetailStatusSoldout.visibility = View.GONE
    }

    fun setSales() {
        binding.ticketDetailStatusReserve.visibility = View.GONE
        binding.ticketDetailStatusSoldout.visibility = View.GONE
    }

    enum class Seller_TicketDetailMenu {
        CHANGE_SALES_OPTION, EDIT_OPTION, DELETE_OPTION
    }

    private fun setTicketItemClickListener(ticketItem: TicketItem) {
        startActivity(Intent(this@TicketDetailActivity, TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }
}
