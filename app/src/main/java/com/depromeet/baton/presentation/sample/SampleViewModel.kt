package com.depromeet.baton.presentation.sample

import androidx.lifecycle.LiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor() : BaseViewModel() {

    private val _viewEvent: SingleLiveEvent<ViewEvent> = SingleLiveEvent()
    val viewEvent: LiveData<ViewEvent> = _viewEvent

    fun doCommand() {
        _viewEvent.postValue(ViewEvent.ShowToast("ShowToast event occurred"))
    }

    sealed class ViewEvent {
        data class ShowToast(val message: String) : ViewEvent()
    }
}