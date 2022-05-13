package com.depromeet.baton.presentation.ui.search

import android.view.View
import com.depromeet.baton.databinding.ListItemHashTagBinding
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.presentation.base.BaseViewHolder

class HashTagViewHolder(view: View) : BaseViewHolder<ListItemHashTagBinding>(view) {

    fun bind(hashTag: BatonHashTag, onClick: (BatonHashTag) -> Unit) {
        binding.chip.text = hashTag.displayTitle
        binding.chip.setOnClickListener { onClick(hashTag) }
    }
}