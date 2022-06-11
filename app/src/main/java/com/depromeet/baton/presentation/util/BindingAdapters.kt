package com.depromeet.baton.presentation.util

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.bds.component.*
import com.depromeet.bds.utils.toPx
import com.depromeet.bds.component.BdsBackwardAppBar
import com.depromeet.bds.component.BdsChoiceChip
import com.depromeet.bds.component.BdsComponentTextField
import com.depromeet.bds.component.BdsFilter

@BindingAdapter("bds_text", "isSelected")
fun setBdsFilterChip(view: BdsFilter, text: String?, isSelected: Boolean) {
    if(text!=null)
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

@BindingAdapter("image", "roundedCorners")
fun ImageView.bindImage(uri: String?, roundedCorners: Boolean) {
    if (uri != null) {
        if (roundedCorners) {
            Glide.with(context)
                .load(uri)
                .transform(RoundedCorners(10.toPx()))
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
    if (text != null) view.text = text
}

@BindingAdapter("isChecked")
fun setBdsCheckbox(view: BdsCheckbox, isChecked: Boolean) {
    view.isChecked = isChecked
}

@BindingAdapter("bds_title")
fun setBdsAppbarTitle(view: BdsBackwardAppBar, title: String?) {
    title?.let {
        view.setTitle(title)
    }
}

@BindingAdapter("periodChecked")
fun addTextChangeListener(view: EditText, isPeriodChecked: Boolean) {
    if (isPeriodChecked) {
        view.doAfterTextChanged {
            dateWithDotFormat(it.toString(), view)
        }
    }
}

