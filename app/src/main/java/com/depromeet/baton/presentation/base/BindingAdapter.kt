package com.depromeet.baton.presentation.base

import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("showOnLoading")
fun LottieAnimationView.showOnLoading(responseState: UIState) {
    visibility = if (responseState is UIState.Loading)
        View.VISIBLE
    else
        View.GONE
}


@BindingAdapter("uiState")
fun setUIStateForLoadedContent(view: View,responseState: UIState) {
    view.visibility  = when (responseState){
        is UIState.HasData ->  View.VISIBLE
        else ->View.GONE
    }
}


@BindingAdapter("btnState")
fun setBtnStateForLoadedContent(view: View,responseState: UIState) {
    view.isEnabled  = when (responseState){
        is UIState.HasData ->  true
        else -> false
    }
}


@BindingAdapter("emptyState")
fun setUIStateForEmptyView(view: View,responseState: UIState) {
    view.visibility  =  when(responseState){
        is UIState.NoData  -> View.VISIBLE
        else -> View.GONE
    }

}

@BindingAdapter("defaultState")
fun setUIStateForDefaultView(view: View,responseState: UIState) {
    view.visibility  =  when(responseState){
        is UIState.Init  -> View.VISIBLE
        else -> View.GONE
    }

}

