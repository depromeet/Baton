package com.depromeet.baton.presentation.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by activityViewModels()

    private val recentFragment = RecentSearchFragment()
    private val detailFragment = SearchDetailFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setImeListener {
            Timber.d("beanbean ime > ${it.text}")
            viewModel.searchKeyword(it.text.toString())
        }

        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchKeyword
                    .map { it.isBlank()  }
                    .collect { showRecent ->
                        if (showRecent) {
                            childFragmentManager
                                .beginTransaction()
                                .replace(R.id.container_fragment, recentFragment, null)
                                .commit()
                        } else {
                            childFragmentManager
                                .beginTransaction()
                                .replace(R.id.container_fragment, detailFragment, null)
                                .commit()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchBar.setEditText(viewModel.searchKeyword.value)
    }
}
