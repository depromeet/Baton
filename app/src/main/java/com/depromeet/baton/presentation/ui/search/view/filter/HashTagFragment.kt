package com.depromeet.baton.presentation.ui.search.view.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHashTagSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HashTagFragment : BaseFragment<FragmentHashTagSearchBinding>(R.layout.fragment_hash_tag_search) {
    private val filterViewModel: FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
    }
}