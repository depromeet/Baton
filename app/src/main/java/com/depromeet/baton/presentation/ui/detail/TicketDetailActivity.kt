package com.depromeet.baton.presentation.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.BatonApp
import com.depromeet.baton.BatonApp.Companion.TAG
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.databinding.ItemPrimaryOutlineTagBinding
import com.depromeet.baton.databinding.ItemPrimaryTagBinding
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.TicketStatus
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.DEFAULT_ITEM_VIEW
import com.depromeet.baton.presentation.ui.detail.model.TicketOwner
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.baton.presentation.util.TicketIteHorizontalDecoration
import com.depromeet.bds.component.BdsToast
import com.depromeet.bds.utils.toPx
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.selects.select
import timber.log.Timber


@AndroidEntryPoint
class TicketDetailActivity : BaseActivity<ActivityTicketDetailBinding>(R.layout.activity_ticket_detail),
    OnMapReadyCallback {

    enum class TicketSaleStatusOption {
        CHANGE_SALES_OPTION, EDIT_OPTION, DELETE_OPTION
    }

    private val user = TicketOwner.SELLER  //임시 데이터
    private var ticketStatus = TicketStatus.SALE

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    private lateinit var ticketTagAdapter :TicketTagAdapter<ItemPrimaryTagBinding>
    private lateinit var gymTagAdapter: TicketTagAdapter<ItemPrimaryOutlineTagBinding>


    private val viewModel by viewModels<TicketDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mapView = findViewById(R.id.ticket_detail_mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        binding.viewmodel = viewModel
        initView()
        setListener()
        setObserver()
        setListView()
    }



    private fun initView() {

        binding.ticketDetailToolbar.ticketToolbarTv.visibility = View.INVISIBLE

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

    private fun initTicketTag(){
        ticketTagAdapter =TicketTagAdapter(
            R.layout.item_primary_tag,
            arrayListOf(BatonHashTag("친절한 선생님"),BatonHashTag("샤워실 포함") ,BatonHashTag("시설"),
                BatonHashTag("깔끔한 시설")
            ))

        FlexboxLayoutManager(this).apply{
            flexWrap = FlexWrap.WRAP
            flexDirection=FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START

        }.let{
            with(binding){
                ticketDetailInfotagRv.layoutManager = it
                ticketDetailInfotagRv.adapter =ticketTagAdapter
            }
        }
    }

    private fun initGymTag(){
        gymTagAdapter =TicketTagAdapter(
            R.layout.item_primary_outline_tag,
            arrayListOf(BatonHashTag("친절한 선생님"),BatonHashTag("샤워실 포함") ,BatonHashTag("시설"),
                BatonHashTag("깔끔한 시설")
            ))

        FlexboxLayoutManager(this).apply{
            flexWrap = FlexWrap.WRAP
            flexDirection=FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START

        }.let{
            with(binding){
                ticketDetailGymtagRv.layoutManager = it
                ticketDetailGymtagRv.adapter = gymTagAdapter
            }
        }
    }

    private fun initShopMoreItem(){
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

    private fun setListView(){
        initTicketTag()
        initGymTag()
        initShopMoreItem()
    }


    private fun setListener() {
        with(binding) {
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

            ticketDetailCopyBtn.setOnClickListener {
                //TODO : 클립복사
                val sample="http://naver.me/5dxygLoW"
                createClipData(sample)
            }

            setOnMenuListener()
            setScrollListener()
        }
    }

    private fun createClipData(message: String){
        val clipBoardManger : ClipboardManager = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(TAG,message)
        clipBoardManger.setPrimaryClip(clipData)
        this@TicketDetailActivity.BdsToast("주소가 복사되었습니다", binding.ticketDetailFooter.top) .show()

    }

    private fun setOnMenuListener(){
        val menuList = resources.getStringArray(R.array.ticket_detail_bottomsheet_menu).map{BottomMenuItem(it)}
        val onItemClick = object : BottomSheetFragment.Companion.OnItemClick{
            override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
                this@TicketDetailActivity.BdsToast("onItem click  ${selected.listItem}").show()
            }
        }

        val bottomSheetFragment = BottomSheetFragment.newInstance("글메뉴",menuList,
            CHECK_ITEM_VIEW,onItemClick)

        /*with(binding){
            ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {

                 {
                    when (it) {
                        TicketSaleStatusOption.CHANGE_SALES_OPTION.ordinal -> {
                            showChangeSalesOptionDialog()
                        }
                        TicketSaleStatusOption.EDIT_OPTION.ordinal -> {

                        }
                        TicketSaleStatusOption.DELETE_OPTION.ordinal -> {

                        }
                    }
                }
                bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)

        }*/

        bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)

    }



    fun initSellerBottom() {
        val menuList = resources.getStringArray(R.array.ticket_detail_bottomsheet_menu)
        val bottomMenu= menuList.map { it -> BottomMenuItem(it) }
        binding.ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {
<<<<<<<
            val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(
                "글 메뉴", bottomMenu,
                BottomSheetFragment.DEFAULT_ITEM_VIEW
            ) {
                when (it) {
                    TicketSaleStatusOption.CHANGE_SALES_OPTION.ordinal -> {
                        showChangeSalesOptionDialog()
                    }
                    TicketSaleStatusOption.EDIT_OPTION.ordinal -> {

                    }
                    TicketSaleStatusOption.DELETE_OPTION.ordinal -> {
                    }
                }
            }
            bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)
=======

>>>>>>>
        }
    }

    fun initBuyerBottom() {

    }
    
    fun showChangeSalesOptionDialog() {

            /*when (it) {
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
            }*/


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

    private fun setTicketItemClickListener(ticketItem: TicketItem) {
        startActivity(Intent(this@TicketDetailActivity, TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }

    private fun setScrollListener(){
        binding.ticketDetailContent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if ((oldScrollY+binding.ticketDetailGymTv.top -scrollY) <=  binding.ticketDetailGymTv.top)
                binding.ticketDetailToolbar.ticketToolbarTv.visibility = View.VISIBLE
            else  binding.ticketDetailToolbar.ticketToolbarTv.visibility = View.INVISIBLE
        })
    }


    override fun onMapReady(map: NaverMap) {
        this.naverMap = map
        //TODO API에서 받아오기
        val position = LatLng(37.5027643, 127.097320)

        val mCameraPosition = CameraPosition( position, 13.0,)
        naverMap.cameraPosition=mCameraPosition
        val marker =Marker()
        setMark(marker, position, com.depromeet.bds.R.drawable.ic_pin_filled_24)

    }

    private fun setMark(marker: Marker, pos : LatLng, resourceID: Int) {
        //아이콘 지정
        marker.icon = (OverlayImage.fromResource(resourceID))
        //마커 위치
        marker.position=pos
        //마커 표시
        marker.map = naverMap

        marker.height=30.toPx()
        marker.width=30.toPx()

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}

