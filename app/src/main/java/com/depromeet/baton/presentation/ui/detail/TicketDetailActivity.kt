package com.depromeet.baton.presentation.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.BatonApp
import com.depromeet.baton.BatonApp.Companion.TAG
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.databinding.ItemPrimaryOutlineTagBinding
import com.depromeet.baton.databinding.ItemPrimaryTagBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.model.TicketStatus
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.WebActivity
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.DEFAULT_ITEM_VIEW
import com.depromeet.baton.presentation.ui.detail.adapter.TicketImgRvAdapter
import com.depromeet.baton.presentation.ui.detail.adapter.TicketMoreAdapter
import com.depromeet.baton.presentation.ui.detail.adapter.TicketTagAdapter
import com.depromeet.baton.presentation.ui.detail.viewModel.*
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter.Companion.SCROLL_TYPE_HORIZONTAL
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter.Companion.SCROLL_TYPE_VERTICAL
import com.depromeet.baton.presentation.util.TicketIteHorizontalDecoration
import com.depromeet.baton.presentation.util.*
import com.depromeet.baton.util.BatonSpfManager
import com.depromeet.bds.component.BdsToast
import com.depromeet.bds.utils.toPx
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TicketDetailActivity : BaseActivity<ActivityTicketDetailBinding>(R.layout.activity_ticket_detail),
    OnMapReadyCallback {

    private lateinit var mapView: MapView
    private  var naverMap: NaverMap? =null

    private lateinit var ticketTagAdapter : TicketTagAdapter<ItemPrimaryTagBinding>
    private lateinit var gymTagAdapter: TicketTagAdapter<ItemPrimaryOutlineTagBinding>
    private val ticketItemRvAdapter = TicketItemRvAdapter(SCROLL_TYPE_HORIZONTAL, ::setTicketItemClickListener )
    private val ticketImgRvAdapter = TicketImgRvAdapter(this)

    @Inject lateinit var spfManager : BatonSpfManager

    private val viewModel  by viewModels<TicketDetailViewModel>()
    private val bottomViewModel by viewModels<TicketDetailBottomViewModel>()
    private val ticketMoreViewModel by viewModels<TicketMoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /**getExtra 로 넘겨 받은 ticketId viewmodel 에 전달됨**/
        mapView = findViewById(R.id.ticket_detail_mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initView()
        setListener()
        setObserver()
    }


    private fun initView() {
        binding.ticketDetailToolbar.setTitleVisible(View.INVISIBLE)
        initImgList()
        setListView()
    }

    private fun setObserver() {

        viewModel.ticketState.observe(
            this@TicketDetailActivity , Observer { uiState -> handleTicketUiState(uiState) }
        )

        viewModel.netWorkState
            .flowWithLifecycle(lifecycle)
            .onEach(::handleTicketNetwork)
            .launchIn(lifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleTicketViewEvents)
            .launchIn(lifecycleScope)

        ticketMoreViewModel.networkState
            .flowWithLifecycle(lifecycle)
            .onEach {status ->
                when(status){
                    is TicketMoreNetwork.Failure -> { this@TicketDetailActivity.BdsToast(status.msg).show() }
                }
            }.launchIn(lifecycleScope)

        ticketMoreViewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach{state -> ticketItemRvAdapter.submitList(state)}
            .launchIn(lifecycleScope)
    }


    private fun handleTicketUiState (uiState  : TicketDetailViewModel.DetailTicketInfoUiState){
        binding.ticketState = uiState
        if( naverMap!=null )setMarkerPosition()
        bottomViewModel.updateBottomUistate(uiState.ticket.isOwner)
        ticketTagAdapter.initList(uiState.ticket.infoHashs)
        gymTagAdapter.initList(uiState.ticket.tags)
        ticketImgRvAdapter.submitList(uiState.ticket.imgList.map { it.url })
    }

    private fun handleTicketNetwork(status :TicketDetailNetWork ){
        when(status){
            is TicketDetailNetWork.Failure ->{
                this@TicketDetailActivity.BdsToast(status.msg).show()
            }
            is TicketDetailNetWork.Loading ->{
                binding.ticketDetailProgress.visibility = View.VISIBLE
            }
            is TicketDetailNetWork.Success ->{
                binding.ticketDetailProgress.visibility = View.GONE
                binding.ticketDetailLoadingIv.visibility= View.GONE
            }
        }
    }


    private fun handleTicketViewEvents(viewEvents: List<DetailViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                DetailViewEvent.EventClickChat->{
                    //TODO showChatBottom
                }
                DetailViewEvent.EventClickLike->{
                    if(viewModel.ticketState.value!!.ticket.isLikeTicket)
                        this@TicketDetailActivity.BdsToast("관심 상품이 등록되었습니다.",binding.ticketDetailFooter.top).show()

                    binding.ticketDetailLikeBtn.toggle()
                }
                DetailViewEvent.EventClickDelete->{
                    finish()
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun setListView(){
        initTicketTag()
        initGymTag()
        initShopMoreItem()
    }


    private fun setListener() {
        with(binding) {
            ticketDetailToolbar.setOnBackwardClick { onBackPressed() }
            ticketDetailToolbar.setOnIconClick{ onClickMenu() }

            ticketDetailUrlBtn.setOnClickListener {
                val url = viewModel.ticketState.value?.ticket!!.detailUrl
                startActivity(WebActivity.start(this@TicketDetailActivity,url))
            }

            ticketDetailCopyBtn.setOnClickListener {
                val address= viewModel.ticketState.value?.ticket!!.location.address
                createClipData(address)
            }

            setScrollListener()
        }
    }

    /** 회원권 정보 recyclerview **/
    private fun initTicketTag(){
        ticketTagAdapter = TicketTagAdapter(R.layout.item_primary_tag)

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

    /** 헬스장 추가 정보 recyclerview **/
    private fun initGymTag(){
        gymTagAdapter = TicketTagAdapter(
            R.layout.item_primary_outline_tag)
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

    /** 회원권이미지 recyclerview **/
    private fun initImgList(){
        with(binding){
            val mLayoutManager = LinearLayoutManager(this@TicketDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            ticketDetailImgRv.adapter = ticketImgRvAdapter
            ticketDetailImgRv.layoutManager = mLayoutManager
            val snapHelper =  PagerSnapHelper()
            snapHelper.attachToRecyclerView(ticketDetailImgRv)
            ticketDetailImgRv.onFlingListener = snapHelper
            ticketDetailImgRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    ticketDetailImgStartTv.text =((recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition()+1).toString()
                }
            })
        }
    }

    /** 추천 회원권 recyclerview **/
    private fun initShopMoreItem(){
        with(binding) {
            val mLayoutManager = LinearLayoutManager(this@TicketDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            ticketDetailRv.addItemDecoration(TicketIteHorizontalDecoration())
            ticketDetailRv.adapter = ticketItemRvAdapter
            ticketDetailRv.layoutManager = mLayoutManager
            ticketItemRvAdapter.submitList(
                ticketMoreViewModel.uiState.value
            )
        }
    }


    private fun onClickMenu(){
        if(bottomViewModel.uiState.value.isOwner){
            showBottom(DEFAULT_ITEM_VIEW, DetailBottomOption.SELLER,onItemClick = sellerItemClick )
        }else{
            showBottom(DEFAULT_ITEM_VIEW, DetailBottomOption.BUYER,onItemClick = buyerItemClick)
        }
    }


    private fun setScrollListener(){
        binding.ticketDetailContent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if ((oldScrollY+binding.ticketDetailGymTv.top -scrollY) <=  binding.ticketDetailGymTv.top)
                binding.ticketDetailToolbar.setTitleVisible(View.VISIBLE)
            else  binding.ticketDetailToolbar.setTitleVisible(View.INVISIBLE)
        })
    }

    private fun createClipData(message: String){
        val clipBoardManger : ClipboardManager = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(TAG,message)
        clipBoardManger.setPrimaryClip(clipData)
        this@TicketDetailActivity.BdsToast("주소가 복사되었습니다", binding.ticketDetailFooter.top) .show()
    }

    /** bottom Item Click Listener **/
    private val statusItemClick = object : BottomSheetFragment.Companion.OnItemClick{
        override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
            when(index){
                0 -> viewModel.ticketStatusHandler(TicketStatus.SALE)
                1 -> viewModel.ticketStatusHandler(TicketStatus.RESERVATION)
                2 -> viewModel.ticketStatusHandler(TicketStatus.SOLDOUT)
            }
        }
    }
    /** bottom Item Click Listener **/
    private val buyerItemClick = object : BottomSheetFragment.Companion.OnItemClick{
        override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
            showBottom(DEFAULT_ITEM_VIEW, DetailBottomOption.REPORT, reportItemClick)
        }
    }
    /** bottom Item Click Listener **/
    private val sellerItemClick= object : BottomSheetFragment.Companion.OnItemClick{
        override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
            when(index){
                0 -> run{
                    showBottom(CHECK_ITEM_VIEW, DetailBottomOption.STATUS, statusItemClick)

                }
                1 -> { //delete
                    viewModel.deleteTicket() // Api 호출
                }
            }
        }
    }
    /** bottom Item Click Listener **/
    private val reportItemClick = object : BottomSheetFragment.Companion.OnItemClick{
        override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
            this@TicketDetailActivity.BdsToast("신고가 접수되었습니다.",binding.ticketDetailFooter.top).show()
            viewModel.reportTicket(index)
        }
    }

    /** 채팅 Bottom 외 **/
    private fun showBottom(viewType : Int, status : DetailBottomOption, onItemClick: BottomSheetFragment.Companion.OnItemClick){
        bottomViewModel.setBottomMenu(status)
        val menuList =
            if(bottomViewModel.uiState.value.option != DetailBottomOption.STATUS)
                bottomViewModel.uiState.value.menuList.map { BottomMenuItem(it) }
            else bottomViewModel.uiState.value.menuList.mapIndexed {
                    index, s ->  BottomMenuItem(s, index == viewModel.ticketState.value!!.ticket.ticketStatus.ordinal)
            }
        val bottomSheetFragment = BottomSheetFragment.newInstance(status.title,menuList!!,
            viewType,onItemClick)
        bottomSheetFragment.show(supportFragmentManager, BatonApp.TAG)
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        startActivity(TicketDetailActivity.start(this,ticketId = ticketItem.id))
    }



    /** Naver MAP Init **/
    override fun onMapReady(map: NaverMap) {
        runBlocking {
            val mapInit = launch {
               this@TicketDetailActivity.naverMap = map
           }
           mapInit.join()
           setMarkerPosition()
           setMapListener()
        }
    }
    private fun setMapListener(){
        naverMap!!.setOnMapClickListener{ point, coord->
            showRoadMapView()
        }
    }

    private fun showRoadMapView(){
        val url = viewModel.ticketState.value?.ticket!!.mapUrl
        startActivity(WebActivity.start(this,url))
    }

    private fun setMarkerPosition(){
        if(naverMap !=null){
            viewModel.ticketState.value?.let{
                val position = LatLng(it.ticket.location.latitude,it.ticket.location.longitude)
                val mCameraPosition = CameraPosition(position, 13.0)
                naverMap!!.cameraPosition=mCameraPosition
                val marker =Marker()
                setMark(marker, position, com.depromeet.bds.R.drawable.ic_pin_filled_24)
            }
        }else{
            Timber.e("naver map init failure")
        }
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
        ticketMoreViewModel.initState()
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

    companion object {
        fun start(context: Context,ticketId: Int):Intent{
            val intent = Intent(context, TicketDetailActivity::class.java)
            intent.putExtra("ticketId",ticketId)
            return intent
        }
    }
}
