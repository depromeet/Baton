package com.depromeet.baton.presentation.ui.search.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTransactionMethodBinding
import com.depromeet.baton.databinding.FragmentTransactionMethodSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.search.FilterSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionMethodFragment : BaseFragment<FragmentTransactionMethodSearchBinding>(R.layout.fragment_transaction_method_search) {
    private val filterViewModel: FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
    }
}