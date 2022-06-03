package com.depromeet.baton.presentation.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()
    private val recentFragment = RecentSearchFragment()
    private val detailFragment = SearchDetailFragment()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.searchUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)

        searchViewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)


        binding.searchBar.setImeListener {
            Timber.d("beanbean ime > ${it.text}")
            searchViewModel.searchKeyword(it.text.toString())
        }

        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchKeyword
                    .map { it.isBlank() }
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

    private fun handleViewEvents(viewEvents: List<SearchViewModel.ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                SearchViewModel.ViewEvent.ToRecentSearch -> {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_fragment, recentFragment, null)
                        .commit()
                }
                SearchViewModel.ViewEvent.ToHome -> {
                    (activity as MainActivity).moveToHome()
                }
            }
            searchViewModel.consumeViewEvent(viewEvent)
        }
    }

    //퀵에서 온경우 검색어 세팅하고 온거 뿌림
    override fun onResume() {
        super.onResume()
        binding.searchBar.setEditText(searchViewModel.searchKeyword.value)
    }

    //추천 해시태그 검색하고 왔을 때임
    override fun onPause() {
        super.onPause()
        binding.searchBar.setEditText(searchViewModel.searchKeyword.value)
    }
}

data class SearchUiState(
    val onBackBtnClick: () -> Unit,
)

