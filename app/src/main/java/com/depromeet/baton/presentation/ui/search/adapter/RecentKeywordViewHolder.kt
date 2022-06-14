package com.depromeet.baton.presentation.ui.search.adapter

import android.view.View
import com.depromeet.baton.databinding.ListItemKeywordBinding
import com.depromeet.baton.domain.model.RecentSearchKeyword
import com.depromeet.baton.presentation.base.BaseViewHolder

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