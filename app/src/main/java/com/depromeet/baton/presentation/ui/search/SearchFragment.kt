package com.depromeet.baton.presentation.ui.search

import android.os.Bundle
import android.util.Log
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
    private val filterViewModel: FilterSearchViewModel by activityViewModels()
    private val recentFragment = RecentSearchFragment()
    private val detailFragment = SearchDetailFragment()

    override fun onResume() {
        super.onResume()
        searchViewModel.setInitKeyword(binding.searchBar.getText())
    }

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
                        binding.searchBar.setEditText(searchViewModel.searchKeyword.value)  //텍스트 세팅하고 시작(퀵에서 오는 경우 존재)
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
                    searchViewModel.searchKeyword("")
                    (activity as MainActivity).moveToHome()
                }
            }
            searchViewModel.consumeViewEvent(viewEvent)
        }
    }

    override fun onPause() {
        super.onPause()
        searchViewModel.setLastKeyword(binding.searchBar.getText())
        Log.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡonPauseㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "${binding.searchBar.getText()}")
    }
}



data class SearchUiState(
    val onBackBtnClick: () -> Unit,
)

