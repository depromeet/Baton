package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentFilterChipBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterChipFragment : BaseFragment<FragmentFilterChipBinding>(R.layout.fragment_filter_chip) {
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        binding.filterChipFragment = this
    }

    fun setFilterChipClickListener(filterPosition: Int) {
        filterViewModel.setCurrentFilterPosition(filterPosition)

        val bottomFilterFragment = BottomFilterFragment()
        bottomFilterFragment.show(
            childFragmentManager,
            bottomFilterFragment.tag
        )
    }
}