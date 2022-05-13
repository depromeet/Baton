package com.depromeet.baton.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.depromeet.baton.R
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.util.SimpleDiffUtil

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