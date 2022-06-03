package com.depromeet.baton.presentation.util

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.bds.component.BdsChoiceChip
import com.depromeet.bds.component.BdsComponentTextField
import com.depromeet.bds.component.BdsFilter
import com.depromeet.bds.component.BdsTag
import com.depromeet.bds.utils.toPx

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

@BindingAdapter("progress")
fun setWritePostPosition(view: ProgressBar, progress: Int) {
    when (progress) {
        1 -> view.progress = 25
        2 -> view.progress = 50
        3 -> view.progress = 75
        4 -> view.progress = 100
        5 -> view.progress = 100
        else -> view.progress = 0
    }
}

@BindingAdapter("itemDecoration")
fun RecyclerView.bindItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    addItemDecoration(itemDecoration)
}

@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter
}

@BindingAdapter("bds_error_message")
fun BdsComponentTextField.bindErrorMessage(message: String?) {
    this.setError(message)
}

@BindingAdapter("isVisible")
fun View.bindVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("image", "centerCrop")
fun AppCompatImageView.bindImage(uri: String?, centerCrop: Boolean) {
    if (uri != null) {
        if (centerCrop) {
            Glide.with(context)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(4.toPx()))
                .into(this)
        } else {
            Glide.with(context)
                .load(uri)
                .into(this)
        }
    }
}

@BindingAdapter("bds_text")
fun setBdsTag(view: BdsTag, text: String?) {
    if(text!=null) view.text = text
}