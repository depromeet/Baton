package com.depromeet.baton.presentation.ui.search.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSearchDetailBinding
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import com.depromeet.baton.presentation.ui.search.viewmodel.SearchViewModel
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchDetailFragment : BaseFragment<FragmentSearchDetailBinding>(R.layout.fragment_search_detail) {

    private val filterSearchViewModel: FilterSearchViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var ticketItemRvAdapter: TicketItemRvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = filterSearchViewModel
        searchViewModel.setCurrentLevel(1)
        setTicketItemRvAdapter()

        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchKeyword
                    .collect { keyWord ->
                        if (keyWord.isNotEmpty() && keyWord != searchViewModel.lastSearchKeyword.value) {
                            when (BatonHashTag(keyWord).displayTitle) {
                                HashTag.KIND_TEACHER.value -> setHashTagFilter(HashTag.KIND_TEACHER)
                                HashTag.SYSTEMATIC_CLASS.value -> setHashTagFilter(HashTag.SYSTEMATIC_CLASS)
                                HashTag.CUSTOMIZED_CARE.value -> setHashTagFilter(HashTag.CUSTOMIZED_CARE)
                                HashTag.SPACIOUS_FACILITIES.value -> setHashTagFilter(HashTag.SPACIOUS_FACILITIES)
                                HashTag.VARIOUS_EQUIPMENT.value -> setHashTagFilter(HashTag.VARIOUS_EQUIPMENT)
                                HashTag.MANY_PEOPLE.value -> setHashTagFilter(HashTag.MANY_PEOPLE)
                                HashTag.QUIET_AMBIENCE.value -> setHashTagFilter(HashTag.QUIET_AMBIENCE)
                                HashTag.STATION_AREA.value -> setHashTagFilter(HashTag.STATION_AREA)
                                HashTag.NEW_EQUIPMENT.value -> setHashTagFilter(HashTag.NEW_EQUIPMENT)
                                HashTag.AGREEMENT.value -> setHashTagFilter(HashTag.AGREEMENT)
                                HashTag.LESS_PEOPLE.value -> setHashTagFilter(HashTag.LESS_PEOPLE)
                            }
                            when (keyWord) {
                                TicketKind.PT.value -> setTicketKindFilter(TicketKind.PT)
                                TicketKind.HEALTH.value -> setTicketKindFilter(TicketKind.HEALTH)
                                TicketKind.PILATES_YOGA.value -> setTicketKindFilter(TicketKind.PILATES_YOGA)
                                TicketKind.ETC.value -> setTicketKindFilter(TicketKind.ETC)
                            }
                        }
                    }
            }
        }
    }

    private fun setTicketKindFilter(ticket: TicketKind) {
        filterSearchViewModel.setTicketKind(ticket, true, true)
    }

    private fun setHashTagFilter(tag: HashTag) {
        filterSearchViewModel.setHashTag(tag, true, true)
    }

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

        filterSearchViewModel.filteredTicketList.observe(viewLifecycleOwner) {
            ticketItemRvAdapter.submitList(it)
        }
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
       TicketDetailActivity.start(requireContext(), ticketItem.id)
    }
}
