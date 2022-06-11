package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
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
        binding.tvHomeLocation.text = if (spfManager.getAddress().roadAddress != "") spfManager.getAddress().roadAddress.slice(0..5) + "..."
        else "위치 설정"
    }

    private fun handleViewEvents(viewEvents: List<HomeViewModel.HomeViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                HomeViewModel.HomeViewEvent.ToLocation -> AddressActivity.start(requireContext())

                HomeViewModel.HomeViewEvent.ToSearch -> (activity as MainActivity).moveToSearch() //todo 검색 keyword 초기화

                HomeViewModel.HomeViewEvent.ToAlarm -> (activity as MainActivity).moveToChatting()

                HomeViewModel.HomeViewEvent.ToHowTo -> HowToUseActivity.start(requireContext())

                HomeViewModel.HomeViewEvent.ToQuickHealth -> goToQuick(TicketKind.HEALTH.value)

                HomeViewModel.HomeViewEvent.ToQuickPt -> goToQuick(TicketKind.PT.value)

                HomeViewModel.HomeViewEvent.ToQuickPilates -> goToQuick(TicketKind.PILATES_YOGA.value)

                HomeViewModel.HomeViewEvent.ToQuickEtc -> goToQuick(TicketKind.ETC.value)

                HomeViewModel.HomeViewEvent.ToWritePost -> WritePostActivity.start(requireContext())

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
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, ::setTicketItemClickListener)
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
        //sample
        val ticketId =1
        startActivity(TicketDetailActivity.start(requireContext(),ticketId))
    }
}



