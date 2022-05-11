package com.depromeet.baton.presentation.util

import android.widget.ProgressBar
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

@BindingAdapter("progress")
fun setWritePostPosition(view: ProgressBar, progress: Int) {
    when (progress) {
        1 -> view.progress = 25
        2 -> view.progress = 50
        3 -> view.progress = 75
        4 -> view.progress = 100
        else -> view.progress = 0
    }
}
