package com.depromeet.baton.presentation.ui.writepost.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
) : BaseViewModel() {
    private val _currentPosition = MutableLiveData(1)
    val currentPosition: LiveData<Int> = _currentPosition

    fun setNextLevel(nextLevel: Boolean = true) {
        if (nextLevel) { //다음버튼
            when (_currentPosition.value) {
                1 -> nextLevelEvent(GO_TO_TICKET_INFO)
                2 -> nextLevelEvent(GO_TO_TRANSACTION_METHOD)
                3 -> nextLevelEvent(GO_TO_DESCRIPTION)
            }
            _currentPosition.value = _currentPosition.value?.plus(1)
        } else {
            _currentPosition.value = _currentPosition.value?.minus(1)
        }
    }

    private fun nextLevelEvent(level: Int) = viewEvent(level)

    companion object {
        const val GO_TO_TICKET_INFO = 2
        const val GO_TO_TRANSACTION_METHOD = 3
        const val GO_TO_DESCRIPTION = 4
    }
}
