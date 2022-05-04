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

    private val _isGymChecked = MutableLiveData<Boolean>()
    val isGymChecked: LiveData<Boolean> = _isGymChecked

    private val _isPtChecked = MutableLiveData<Boolean>()
    val isPtChecked: LiveData<Boolean> = _isPtChecked

    private val _isPilatesYogaChecked = MutableLiveData<Boolean>()
    val isPilatesYogaChecked: LiveData<Boolean> = _isPilatesYogaChecked

    private val _isEtcChecked = MutableLiveData<Boolean>()
    val isEtcChecked: LiveData<Boolean> = _isEtcChecked

    /*position, list관련*/
    val filterTypeOrderList = ListLiveData<String>()

    private val _currentFilterPosition = MutableLiveData<Int>()
    val currentFilterPosition: LiveData<Int> = _currentFilterPosition

    private val _currentFilterBottomPosition = MutableLiveData<Int>()

    private val _filterChipList = MutableLiveData<MutableList<Pair<String, Boolean>>>()
    val filterChipList: LiveData<MutableList<Pair<String, Boolean>>> = _filterChipList

    init {
        //초기 필터 리스트 상태 세팅
        setFilterPosition()
        //바텀 탭레이아웃에게 그려야하는 필터순서 리스트로 전달하기 위함
        filterTypeOrderList.value = filterChipList.value?.toMap()?.keys?.toMutableList()
    }

    private fun setFilterPosition() {
        //필터 순서, 필터 선택여부 동적관리 -> 여기서만 관리해주도록
        _filterChipList.value = mutableListOf(
            FilterType.Alignment.value to false,
            FilterType.TicketKind.value to (_isTicketKindFiltered.value ?: false),
            FilterType.Term.value to false,
            FilterType.Price.value to false,
            FilterType.TransactionMethod.value to false,
            FilterType.AdditionalOptions.value to false,
            FilterType.HashTag.value to false,
        )
    }

    //홈에서 필터 클릭해서 올 때 바텀에서 어떤 포지션 탭 띄워야하는지 세팅하는 곳
    fun setCurrentFilterPosition(currentFilterPosition: Int) {
        _currentFilterPosition.value = currentFilterPosition
    }

    //홈에서 클릭해서 올 때 포지션, 바텀에서 초기화 누르는 탭 포지션이 다를 수 있음
    //같은 position 데이터 쓰면 홈에서 클릭해서 올 때 포지션 observe하는 부분도 변해서 따로 관리
    fun setCurrentFilterBottomPosition(currentFilterBottomPosition: Int) {
        _currentFilterBottomPosition.value = currentFilterBottomPosition
    }

    //바텀 포지션에 따라서 필터 초기화, position이 바뀌어도 Type으로 판단하도록
    fun filterReset() {
        val currentPositionType = filterChipList.value?.get(_currentFilterBottomPosition.value ?: return)?.first
        when (currentPositionType) {
            FilterType.TicketKind.value -> {
                _isGymChecked.value = false
                _isPtChecked.value = false
                _isPilatesYogaChecked.value = false
                _isEtcChecked.value = false
                _ticketKindList.clear()
                setIsTicketKindFiltered()
            }
        }
    }

    //양도권종류 아이템 클릭여부를 관리
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

    //양도권종류 하나라도 선택되었는지 여부->홈에서 양도권 종류 필터링 여부
    private fun setIsTicketKindFiltered() {
        _isTicketKindFiltered.value = _ticketKindList.isListNotEmpty()
        setFilterPosition()
    }
}