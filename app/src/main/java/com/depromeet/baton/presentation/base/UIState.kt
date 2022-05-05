package com.depromeet.baton.presentation.base

import androidx.annotation.StringRes

sealed class UIState{
    object Init : UIState()
    object Loading : UIState()
    object HasData : UIState()
    object NoData : UIState()
    class Error(@StringRes val errorMsgId:Int = 0) : UIState()
}

enum class ErrorMessage(){

}