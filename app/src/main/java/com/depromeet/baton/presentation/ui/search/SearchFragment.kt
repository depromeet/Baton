package com.depromeet.baton.presentation.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.bds.component.BdsSearchBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setImeListener {
            Timber.d("beanbean ime > ${it.text}")
            viewModel.searchKeyword(it.text.toString())
        }

        childFragmentManager
            .beginTransaction()
            .replace(R.id.container_fragment, RecentSearchFragment(), null)
            .commit()
    }
}
