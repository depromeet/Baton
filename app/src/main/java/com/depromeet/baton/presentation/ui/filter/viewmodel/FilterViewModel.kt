package com.depromeet.baton.presentation.ui.filter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.ListLiveData
import com.depromeet.baton.presentation.util.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    /*양도권 종류*/
    private val _ticketKindList = ListLiveData<String>()

    private val _isTicketKindFiltered = MutableLiveData<Boolean>()
    val isTicketKindFiltered: LiveData<Boolean> = _isTicketKindFiltered

    private val _isGymChecked = MutableLiveData<Boolean>()
    val isGymChecked: LiveData<Boolean> = _isGymChecked

    private val _isPtChecked = MutableLiveData<Boolean>()
    val isPtChecked: LiveData<Boolean> = _isPtChecked

    private val _isPilatesYogaChecked = MutableLiveData<Boolean>()
    val isPilatesYogaChecked: LiveData<Boolean> = _isPilatesYogaChecked

    private val _isEtcChecked = MutableLiveData<Boolean>()
    val isEtcChecked: LiveData<Boolean> = _isEtcChecked

    fun setTicketKind(ticket: TicketKind, isChecked: Boolean) {
        when (ticket) {
            TicketKind.GYM -> _isGymChecked.value = isChecked
            TicketKind.PT -> _isPtChecked.value = isChecked
            TicketKind.PILATES_YOGA -> _isPilatesYogaChecked.value = isChecked
            TicketKind.ETC -> _isEtcChecked.value = isChecked
        }

        if (isChecked) _ticketKindList.add(resourcesProvider.getString(ticket.value))
        else _ticketKindList.remove(resourcesProvider.getString(ticket.value))

        setIsTicketKindFiltered()
    }

    fun filterReset(filterType: FilterType) {
        when (filterType) {
            FilterType.TicketKind -> {
                _isGymChecked.value = false
                _isPtChecked.value = false
                _isPilatesYogaChecked.value = false
                _isEtcChecked.value = false
                _ticketKindList.clear()
                setIsTicketKindFiltered()
            }
        }
    }

    private fun setIsTicketKindFiltered() {
        _isTicketKindFiltered.value = _ticketKindList.isListNotEmpty()
    }
}