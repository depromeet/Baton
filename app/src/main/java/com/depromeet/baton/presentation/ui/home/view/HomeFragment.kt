package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.getPositionFromFilterType
import com.depromeet.baton.presentation.ui.filter.view.BottomFilterFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeFragment = this
        binding.filterViewModel = filterViewModel
    }

    fun setFilterChipClickListener(filterType: FilterType) {
        val bottomFilterFragment = BottomFilterFragment().apply {
            arguments = bundleOf(CHECKED_FILTER_POSITION_KEY to getPositionFromFilterType(filterType))
        }
        bottomFilterFragment.show(
            childFragmentManager,
            bottomFilterFragment.tag
        )
    }

    companion object {
        const val CHECKED_FILTER_POSITION_KEY = "com.depromeet.baton.presentation.ui.home.view CHECKED_FILTER_POSITION_KEY"
    }
}