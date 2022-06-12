package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentFilterChipBinding
import com.depromeet.baton.domain.model.Alignment
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterChipFragment : BaseFragment<FragmentFilterChipBinding>(R.layout.fragment_filter_chip) {

    private val filterViewModel: FilterViewModel by activityViewModels()

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        binding.filterChipFragment = this
        setAlignClickListener()

    }

    fun setFilterChipClickListener(filterPosition: Int) {
        filterViewModel.setCurrentFilterPosition(filterPosition)


        val bottomFilterFragment = BottomFilterFragment()
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
                        0 -> filterViewModel.setAlignment(Alignment.DISTANCE)
                        1 -> filterViewModel.setAlignment(Alignment.LOWER_PRICE)
                        2 -> filterViewModel.setAlignment(Alignment.VIEWS)
                        3 -> filterViewModel.setAlignment(Alignment.REMAIN_DAY)
                        4 -> filterViewModel.setAlignment(Alignment.REMAIN_NUMBER)
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
