package com.depromeet.baton.presentation.base

sealed class UIState{
    object Init : UIState()
    object Loading : UIState()
    object HasData : UIState()
    object NoData : UIState()
    class Error(val errorMsgId: String) : UIState()
}
