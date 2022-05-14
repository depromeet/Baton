package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentFilterChipBinding
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

        val list = arrayListOf("가까운 거리순", "낮은 가격순", "인기순", "남은 기간 많은 순")
        val menu = list.map {BottomMenuItem(it) }.toMutableList()
        binding.llFilterChipAlign.setOnClickListener {
            val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment("정렬 순", menu, BottomSheetFragment.CHECK_ITEM_VIEW) {
                //TODO something
                /*
                    when(it){
                               0->  // menu pos에 따라 처리
                               1->
                      }
                */

            }
            bottomSheetFragment.show(
                childFragmentManager,
                bottomSheetFragment.tag
            )
        }
    }
}