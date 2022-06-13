package com.depromeet.baton.presentation.ui.search.view.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentFilterChipSearchBinding
import com.depromeet.baton.domain.model.Alignment
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterChipSearchFragment : BaseFragment<FragmentFilterChipSearchBinding>(R.layout.fragment_filter_chip_search) {

    private val filterSearchViewModel: FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterSearchViewModel
        binding.filterChipFragment = this
        setAlignClickListener()

    }

    override fun onResume() {
        super.onResume()
          filterSearchViewModel.setFilterTypeOrderList()
    }

    fun setFilterChipClickListener(filterPosition: Int) {
        filterSearchViewModel.setCurrentFilterPosition(filterPosition)


        val bottomFilterFragment = BottomFilterSearchFragment()
        bottomFilterFragment.show(
            childFragmentManager,
            bottomFilterFragment.tag
        )
    }

    private fun setAlignClickListener() {
        val menu = arrayListOf("가까운 거리순", "낮은 가격순", "인기순", "남은 기간 많은 순", "남은 횟수 많은 순").map { BottomMenuItem(it) }
        binding.tvBdsfilterSortingAlignment.setOnClickListener {
            val onItemClick = object : BottomSheetFragment.Companion.OnItemClick {
                override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
                    when (index) {
                        0 -> filterSearchViewModel.setAlignment(Alignment.DISTANCE)
                        1 -> filterSearchViewModel.setAlignment(Alignment.LOWER_PRICE)
                        2 -> filterSearchViewModel.setAlignment(Alignment.VIEWS)
                        3 -> filterSearchViewModel.setAlignment(Alignment.REMAIN_DAY)
                        4 -> filterSearchViewModel.setAlignment(Alignment.REMAIN_NUMBER)
                    }
                }
            }

            val bottomSheetFragment = BottomSheetFragment.newInstance(
                "정렬", menu,
                BottomSheetFragment.CHECK_ITEM_VIEW, onItemClick
            )

            bottomSheetFragment.show(
                childFragmentManager,
                bottomSheetFragment.tag
            )
        }
    }
}
