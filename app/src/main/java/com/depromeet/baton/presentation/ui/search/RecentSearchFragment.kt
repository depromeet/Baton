package com.depromeet.baton.presentation.ui.search

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentRecentSearchBinding
import com.depromeet.baton.domain.model.RecentSearchKeyword
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.baton.util.ListPaddingDecoration
import com.depromeet.bds.utils.toPx
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxItemDecoration.BOTH
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecentSearchFragment :
    BaseFragment<FragmentRecentSearchBinding>(R.layout.fragment_recent_search) {

    private val filterViewModel: FilterSearchViewModel by activityViewModels()
    private val viewModel: RecentSearchViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val hashTagAdapter by lazy {
        HashTagAdapter {
            searchViewModel.searchKeyword(it.title)
        }
    }
    private val keywordAdapter by lazy {
        RecentKeywordAdapter(
            onClick = { searchViewModel.searchKeyword(it.title) },
            onDelete = { viewModel.deleteKeyword(it) }
        )
    }

    override fun onResume() {
        super.onResume()
        filterViewModel.filterReset()
        searchViewModel.searchKeyword("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.setCurrentLevel(0)
        binding.containerHashTag.apply {
            adapter = hashTagAdapter
            itemAnimator = null
            val itemDecoration = FlexboxItemDecoration(requireContext()).apply {
                val drawable = GradientDrawable().apply {
                    this.setSize(0, 8.toPx())
                    this.setColor(Color.TRANSPARENT)
                }
                setDrawable(drawable)
                setOrientation(BOTH)
            }
            addItemDecoration(itemDecoration)
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
        }

        binding.listRecent.apply {
            adapter = keywordAdapter
            itemAnimator = null
            val itemDecoration = ListPaddingDecoration(8)
            addItemDecoration(itemDecoration)
        }

        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recommendedHashTags
                    .collect { tags ->
                        Timber.d("beanbean > $tags")
                        hashTagAdapter.submitList(tags)
                    }
            }

        }
        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentSearchKeywords
                    .collect { keywords ->
                        Timber.d("beanbean > $keywords")

                        val isEmpty = keywords.isEmpty()

                        binding.emptyView.isVisible = isEmpty
                        binding.listRecent.isVisible = !isEmpty

                        keywordAdapter.submitList(keywords) {
//                            if (keywords.isNotEmpty()) binding.listRecent.scrollToPosition(0)
                        }
                    }
            }
        }

        viewLifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchKeyword
                    .filter { it.isNotBlank() }
                    .distinctUntilChanged()
                    .collect { viewModel.searchKeyword(RecentSearchKeyword(it)) }
            }
        }

        binding.buttonDeleteAll.setOnClickListener { viewModel.deleteAll() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    filterViewModel.filterReset()
    }
}
