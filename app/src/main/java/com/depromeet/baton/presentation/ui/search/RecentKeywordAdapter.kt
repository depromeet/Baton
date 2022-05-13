package com.depromeet.baton.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.baton.R
import com.depromeet.baton.domain.model.RecentSearchKeyword
import com.depromeet.baton.util.SimpleDiffUtil

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