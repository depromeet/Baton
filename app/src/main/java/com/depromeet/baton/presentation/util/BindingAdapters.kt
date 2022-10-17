package com.depromeet.baton.presentation.util

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.depromeet.baton.R
import com.depromeet.baton.chat.ChatController
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.presentation.ui.chatting.ChatMessageAdapter
import com.depromeet.bds.component.*
import com.depromeet.bds.utils.toPx
import timber.log.Timber


@BindingAdapter("bds_text", "isSelected")
fun setBdsFilterChip(view: BdsFilter, text: String?, isSelected: Boolean) {
    if (text != null)
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
                .centerCrop()
                .into(this)
        }
    }
}

@BindingAdapter("bds_text")
fun setBdsTag(view: BdsTag, text: String?) {
    if (text != null) view.text = text
}

@BindingAdapter("bds_text")
fun setBdsTag(view: BdsButton, text: String?) {
    view.setText(text)
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

fun BdsBackwardAppBar.bindTitle(title: String?) {
    bdsTitle = title
}

@BindingAdapter("messageItem")
fun RecyclerView.bindingItem(uiState: ChatController.MessageUiState) {
    val boundAdapter = this.adapter
    if (boundAdapter is ChatMessageAdapter) {
        boundAdapter.submitList(uiState.messages)
    }
}


@BindingAdapter("messageImg")
fun ImageView.bindImage(message: Message) {
    Glide.with(context)
        .load(message.image ?:com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_56)
        .transform(CircleCrop())
        .apply{
            this.signature(ObjectKey("ask-profile"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        .into(this)
}

@BindingAdapter("messageText")
fun setMessageDayText(text: TextView, message: Message) {
    if(message.isChecked == null ) text.setText("")
    else if(message.isChecked ) text.setText("읽음")
    else text.setText("안읽음")
}

@SuppressLint("ResourceAsColor")
@BindingAdapter("messageTextStatus")
fun setMessageTextStatus(text: TextView, status : String?) {
    if(status =="삭제됨" ){
        text.setTextColor(ContextCompat.getColor(text.context, com.depromeet.bds.R.color.gy60))
    }
}

@SuppressLint("ResourceAsColor")
@BindingAdapter("tradeTextStatus")
fun setTradeTextStatus(text: TextView, status : String?) {
    if(status == "삭제됨" ){
        text.setTextColor(ContextCompat.getColor(text.context, com.depromeet.bds.R.color.gy60))
    }else if(status == "거래완료"){
        text.setTextColor(ContextCompat.getColor(text.context, com.depromeet.bds.R.color.gy70))
    }else text.setTextColor(ContextCompat.getColor(text.context, com.depromeet.bds.R.color.orange50))
}

@BindingAdapter("tint")
fun bindImageTint(imageView: ImageView, colorId: Int) {
    if (colorId == 0) {
        return
    }
    val tint = ContextCompat.getColor(imageView.context, colorId)
    ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(tint))
}


@BindingAdapter("image", "ticketType")
fun ImageView.bindImage(uri: String?, ticketType : String?) {
    if (uri != null ) {
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(4.toPx()))
            .into(this)

    }else{
        var icon = when(ticketType){
            "HEALTH" -> {com.depromeet.bds.R.drawable.ic_empty_health_86}
            "PT" ->{  com.depromeet.bds.R.drawable.ic_empty_pt_86}
            "PILATES_YOGA"->{ com.depromeet.bds.R.drawable.ic_empty_pilates_86}
            else ->{
                com.depromeet.bds.R.drawable.ic_empty_etc_86}
        }

        Glide.with(context)
            .load(icon)
            .centerCrop()
            .into(this)
    }
}

