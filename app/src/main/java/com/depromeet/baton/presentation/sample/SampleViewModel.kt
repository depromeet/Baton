package com.depromeet.baton.presentation.sample

import androidx.lifecycle.LiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor() : BaseViewModel() {

    private val _sampleViewEvent: SingleLiveEvent<SampleViewEvent> = SingleLiveEvent()
    val sampleViewEvent: LiveData<SampleViewEvent> = _sampleViewEvent

    fun doCommand() {
        _sampleViewEvent.postValue(SampleViewEvent.ShowToast("ShowToast event occurred"))
    }

    sealed class SampleViewEvent {
        data class ShowToast(val message: String) : SampleViewEvent()
    }
}