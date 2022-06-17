package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.home.viewmodel.HomeViewModel
import com.depromeet.baton.presentation.ui.search.viewmodel.SearchViewModel
import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.util.BatonSpfManager
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()
    private lateinit var ticketItemRvAdapter: TicketItemRvAdapter

    @Inject
    lateinit var spfManager: BatonSpfManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        initView()
    }

    override fun onResume() {
        super.onResume()
        initLayout()
        filterViewModel.updateFilteredTicketList()
        if (homeViewModel.fromAddress.value == true) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    homeViewModel.checkToolTipState(filterViewModel.ticketCount.value!!, spfManager.getAddress().roadAddress)
                }, 600)
            homeViewModel.setFromAddress(false)
        }
    }

    private fun initView() {
        initLayout()
        setTicketItemRvAdapter()
        setObserve()
    }

    private fun setObserve() {
        homeViewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)

        homeViewModel.homeUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
    }

    private fun initLayout() {
        val roadAddress = spfManager.getAddress().roadAddress

        binding.tvHomeLocation.text = if (roadAddress != "") {
            roadAddress.slice(0..5) + "..."
        } else "위치 설정"
    }

    private fun showToolTip() {
        val balloon = Balloon.Builder(requireContext())
            .setWidthRatio(0.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setElevation(3)
            .setMarginBottom(5)
            .setMarginLeft(16)
            .setTextSize(10f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowDrawableResource(com.depromeet.bds.R.drawable.ic_tooltip_subtract)
            .setArrowSize(10)
            .setArrowPosition(0.57f)
            .setPadding(10)
            .setCornerRadius(4f)
            .setBackgroundColorResource(com.depromeet.bds.R.color.bg)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLayout(com.depromeet.baton.R.layout.tooltip_home)
            .setLifecycleOwner(this)
            .build()
        binding.ctlHomeLocation.showAlignBottom(balloon, 0, 0)
    }

    private fun handleViewEvents(viewEvents: List<HomeViewModel.HomeViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                HomeViewModel.HomeViewEvent.ToLocation -> {
                    homeViewModel.setFromAddress(true)
                    AddressActivity.start(requireContext())
                }

                HomeViewModel.HomeViewEvent.ToSearch -> (activity as MainActivity).moveToSearch() //todo 검색 keyword 초기화

                HomeViewModel.HomeViewEvent.ToAlarm -> (activity as MainActivity).moveToChatting()

                HomeViewModel.HomeViewEvent.ToHowTo -> HowToUseActivity.start(requireContext())

                HomeViewModel.HomeViewEvent.ToQuickHealth -> goToQuick(TicketKind.HEALTH.value)

                HomeViewModel.HomeViewEvent.ToQuickPt -> goToQuick(TicketKind.PT.value)

                HomeViewModel.HomeViewEvent.ToQuickPilates -> goToQuick(TicketKind.PILATES_YOGA.value)

                HomeViewModel.HomeViewEvent.ToQuickEtc -> goToQuick(TicketKind.ETC.value)

                HomeViewModel.HomeViewEvent.ToWritePost -> WritePostActivity.start(requireContext())

                HomeViewModel.HomeViewEvent.ShowToolTip -> showToolTip()
            }
            homeViewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun goToQuick(type: String) {
        searchViewModel.searchKeyword(type)
        (activity as MainActivity).moveToSearch()
    }

    //필터 아이템 어댑터
    private fun setTicketItemRvAdapter() {
        with(binding) {
            ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, ::setTicketItemClickListener, ::setBookmarkDeleteClickListener, ::setBookmarkAddClickListener)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = ticketItemRvAdapter
            rvHome.itemAnimator = null
            itemDecoration = TicketItemVerticalDecoration()
            rvHome.layoutManager = gridLayoutManager
        }


        filterViewModel.filteredTicketList.observe(viewLifecycleOwner) {
            ticketItemRvAdapter.submitList(it)
        }
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(requireContext(), ticketItem.id)
    }

    private fun setBookmarkDeleteClickListener(ticketItem: FilteredTicket) {
        homeViewModel.postBookmark(ticketItem.id)
    }

    private fun setBookmarkAddClickListener(ticketItem: FilteredTicket) {
        homeViewModel.deleteBookmark(ticketItem.id)
    }
}



