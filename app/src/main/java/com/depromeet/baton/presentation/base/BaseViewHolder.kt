package com.depromeet.baton.presentation.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {
    protected val binding: T = checkNotNull(DataBindingUtil.bind(view))
}
