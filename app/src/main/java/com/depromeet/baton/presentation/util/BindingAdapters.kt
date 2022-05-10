package com.depromeet.baton.presentation.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.bds.component.BdsActionChip
import com.depromeet.bds.component.BdsChoiceChip
import com.depromeet.bds.component.BdsFilter

@BindingAdapter("bds_text", "isSelected")
fun setBdsFilterChip(view: BdsFilter, text: String, isSelected: Boolean) {
    view.text = text
    view.isSelected = isSelected
}

@BindingAdapter("isOn", "shape")
fun setBdsChoiceChip(view: BdsChoiceChip, isOn: Boolean, shape: String) {
    if (shape == "outlined") view.setOnAndShape(isOn, 0)
    else view.setOnAndShape(isOn, 1)
}

@BindingAdapter("itemDecoration")
fun RecyclerView.bindItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    addItemDecoration(itemDecoration)
}

@BindingAdapter("rvAdapter")
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
}
