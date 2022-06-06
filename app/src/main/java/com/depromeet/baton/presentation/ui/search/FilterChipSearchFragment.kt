package com.depromeet.baton.presentation.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentFilterChipSearchBinding
import com.depromeet.baton.domain.model.Alignment
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.filter.view.BottomFilterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterChipSearchFragment : BaseFragment<FragmentFilterChipSearchBinding>(R.layout.fragment_filter_chip_search) {

    private val filterViewModel: com.depromeet.baton.presentation.ui.search.FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        binding.filterChipFragment = this
        setAlignClickListener()

    }

    override fun onResume() {
        super.onResume()
        Log.e("ㅡㅡㅡㅡㅡ세팅ㅡㅇㅇㅡㅡㅡㅡㅡ","${filterViewModel.filterChipList.value}")
          filterViewModel.setFilterTypeOrderList()
    }

    fun setFilterChipClickListener(filterPosition: Int) {
        filterViewModel.setCurrentFilterPosition(filterPosition)


        val bottomFilterFragment = com.depromeet.baton.presentation.ui.search.view.BottomFilterFragment()
        bottomFilterFragment.show(
            childFragmentManager,
            bottomFilterFragment.tag
        )
    }

    private fun setAlignClickListener() {
        val menu = arrayListOf("가까운 거리순", "낮은 가격순", "인기순", "남은 기간 많은 순").map { BottomMenuItem(it) }
        binding.tvBdsfilterSortingAlignment.setOnClickListener {
            val onItemClick = object : BottomSheetFragment.Companion.OnItemClick {
                override fun onSelectedItem(selected: BottomMenuItem, index: Int) {
                    when (index) {
                        0 -> filterViewModel.setAlignment(Alignment.RECENT)
                        1 -> filterViewModel.setAlignment(Alignment.LOWER_PRICE)
                        2 -> filterViewModel.setAlignment(Alignment.VIEW)
                        3 -> filterViewModel.setAlignment(Alignment.REMAIN_DAY)
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
