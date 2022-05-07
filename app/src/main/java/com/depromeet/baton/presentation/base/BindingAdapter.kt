package com.depromeet.baton.presentation.base

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("showOnLoading")
fun ProgressBar.showOnLoading(responseState: UIState) {
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

