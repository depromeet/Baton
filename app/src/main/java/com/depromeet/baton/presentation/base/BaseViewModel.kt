package com.depromeet.baton.presentation.base


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {


    private val _viewEvent = MutableLiveData<com.depromeet.baton.presentation.util.Event<Any>>()
    val viewEvent: LiveData<com.depromeet.baton.presentation.util.Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        _viewEvent.value = com.depromeet.baton.presentation.util.Event(content)
    }
}