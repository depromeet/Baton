package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTopBinding
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.home.viewmodel.HomeViewModel
import com.depromeet.baton.presentation.ui.search.viewmodel.SearchViewModel
import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TopFragment : BaseFragment<FragmentTopBinding>(R.layout.fragment_top) {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserve()
        binding.btnHomeMore.setOnClickListener{
            HowToUseActivity.start(requireContext())
        }
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

    private fun handleViewEvents(viewEvents: List<HomeViewModel.HomeViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                HomeViewModel.HomeViewEvent.ToLocation -> {
                    homeViewModel.setFromAddress(true)
                    AddressActivity.start(requireContext())
                }

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
}