package com.depromeet.baton.presentation.ui.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentRecentSearchBinding
import com.depromeet.baton.databinding.ListItemHashTagBinding
import com.depromeet.baton.databinding.ListItemKeywordBinding
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.RecentSearchKeyword
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewHolder
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


val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

class SimpleDiffUtil<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

class HashTagAdapter(
    val onClick: (BatonHashTag) -> Unit = {}
) : ListAdapter<BatonHashTag, HashTagViewHolder>(SimpleDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashTagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_hash_tag, parent, false)
        return HashTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: HashTagViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }
}

class RecentKeywordAdapter(
    val onClick: (RecentSearchKeyword) -> Unit = {},
    val onDelete: (RecentSearchKeyword) -> Unit = {},
) :
    ListAdapter<RecentSearchKeyword, RecentKeywordViewHolder>(SimpleDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentKeywordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_keyword, parent, false)
        return RecentKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentKeywordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, onDelete)
    }
}

class HashTagViewHolder(view: View) : BaseViewHolder<ListItemHashTagBinding>(view) {

    fun bind(hashTag: BatonHashTag, onClick: (BatonHashTag) -> Unit) {
        binding.chip.text = hashTag.displayTitle
        binding.chip.setOnClickListener { onClick(hashTag) }
    }
}

class RecentKeywordViewHolder(view: View) : BaseViewHolder<ListItemKeywordBinding>(view) {
    fun bind(
        keyword: RecentSearchKeyword,
        onClick: (RecentSearchKeyword) -> Unit,
        onDelete: (RecentSearchKeyword) -> Unit,
    ) {
        binding.chip.text = keyword.title
        binding.chip.setOnClickListener { onClick(keyword) }
        binding.chip.setOnXClick { onDelete(keyword) }
    }
}

class ListPaddingDecoration(
    @Dimension(unit = Dimension.DP) private val paddingDp: Int
) : ItemDecoration() {

    private val padding: Int get() = paddingDp.toPx()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        outRect.right = padding
        val adapter = parent.adapter
        if (adapter != null && itemPosition == adapter.itemCount - 1) {
            outRect.right = 0
        }
    }
}

@AndroidEntryPoint
class RecentSearchFragment :
    BaseFragment<FragmentRecentSearchBinding>(R.layout.fragment_recent_search) {

    private val viewModel: RecentSearchViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val hashTagAdapter by lazy {
        HashTagAdapter {
            viewModel.searchKeyword(RecentSearchKeyword(it.title))
        }
    }
    private val keywordAdapter by lazy {
        RecentKeywordAdapter(
            onClick = { viewModel.searchKeyword(it) },
            onDelete = { viewModel.deleteKeyword(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerHashTag.apply {
            adapter = hashTagAdapter
            itemAnimator = null
            val itemDecoration = FlexboxItemDecoration(requireContext()).apply {
                val drawable = GradientDrawable().apply {
                    this.setSize(8.toPx(), 8.toPx())
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
                            binding.listRecent.scrollToPosition(0)
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
}
