package com.depromeet.baton.presentation.util

import androidx.databinding.BindingAdapter
import com.depromeet.bds.component.BdsChoiceChip
import com.depromeet.bds.component.BdsFilter

@BindingAdapter("bds_text", "isSelected")
fun setBdsFilter(view: BdsFilter, text: String, isSelected: Boolean) {
    view.text = text
    view.isSelected = isSelected
}

@BindingAdapter("isOn", "shape")
fun setBdsChoice(view: BdsChoiceChip, isOn: Boolean, shape: String) {
    if (shape == "outlined") view.setOnAndShape(isOn, 0)
    else view.setOnAndShape(isOn, 1)
}
