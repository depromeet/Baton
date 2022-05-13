package com.depromeet.baton.presentation.ui.filter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.ListLiveData
import com.depromeet.baton.presentation.util.MapListLiveData
import com.depromeet.baton.presentation.util.SingleLiveEvent
import com.depromeet.baton.presentation.util.priceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
) : BaseViewModel() {

    private val _allCount = MutableLiveData<Int>(20)
    val allCount: LiveData<Int> = _allCount

    /*양도권 종류*/
    var ticketKindCheckedList = MapListLiveData<TicketKind, Boolean>()

    private val _isTicketKindFiltered = MutableLiveData<Boolean>()

    private val _isGymChecked = MutableLiveData(false)
    val isGymChecked: LiveData<Boolean> = _isGymChecked

    private val _isPtChecked = MutableLiveData(false)
    val isPtChecked: LiveData<Boolean> = _isPtChecked

    private val _isPilatesYogaChecked = MutableLiveData(false)
    val isPilatesYogaChecked: LiveData<Boolean> = _isPilatesYogaChecked

    private val _isEtcChecked = MutableLiveData(false)
    val isEtcChecked: LiveData<Boolean> = _isEtcChecked

    /*거래방법*/
    var transactionMethodCheckedList = MapListLiveData<TransactionMethod, Boolean>()

    private val _isTransactionMethodFiltered = MutableLiveData<Boolean>()

    private val _isFaceChecked = MutableLiveData(false)
    val isFaceChecked: LiveData<Boolean> = _isFaceChecked

    private val _isNonFaceChecked = MutableLiveData(false)
    val isNonFaceChecked: LiveData<Boolean> = _isNonFaceChecked

    private val _isSellerChecked = MutableLiveData(false)
    val isSellerChecked: LiveData<Boolean> = _isSellerChecked

    private val _isConsumerChecked = MutableLiveData(false)
    val isConsumerChecked: LiveData<Boolean> = _isConsumerChecked

    /*추가옵션*/
    private var additionalOptionsCheckedList = MapListLiveData<AdditionalOptions, Boolean>()

    private val _isAdditionalOptionsFiltered = MutableLiveData<Boolean>()

    private val _isShowerRoomChecked = MutableLiveData(false)
    val isShowerRoomChecked: LiveData<Boolean> = _isShowerRoomChecked

    private val _isLockerRoomChecked = MutableLiveData(false)
    val isLockerRoomChecked: LiveData<Boolean> = _isLockerRoomChecked

    private val _isSportWearrChecked = MutableLiveData(false)
    val isSportWearrChecked: LiveData<Boolean> = _isSportWearrChecked

    private val _isGxChecked = MutableLiveData(false)
    val isGxChecked: LiveData<Boolean> = _isGxChecked

    private val _isReTransferChecked = MutableLiveData(false)
    val isReTransferChecked: LiveData<Boolean> = _isReTransferChecked

    private val _isRefundChecked = MutableLiveData(false)
    val isRefundChecked: LiveData<Boolean> = _isRefundChecked

    private val _isHoldingChecked = MutableLiveData(false)
    val isHoldingChecked: LiveData<Boolean> = _isHoldingChecked

    private val _isBargainingChecked = MutableLiveData(false)
    val isBargainingChecked: LiveData<Boolean> = _isBargainingChecked

    /*해시태그*/
    private var hashTagCheckedList = MapListLiveData<HashTag, Boolean>()

    private val _isHashTagFiltered = MutableLiveData<Boolean>()

    private val _isKindTeacherChecked = MutableLiveData(false)
    val isKindTeacherChecked: LiveData<Boolean> = _isKindTeacherChecked

    private val _isSystematicLessonChecked = MutableLiveData(false)
    val isSystematicLessonChecked: LiveData<Boolean> = _isSystematicLessonChecked

    private val _isCustomizedCareChecked = MutableLiveData(false)
    val isCustomizedCareChecked: LiveData<Boolean> = _isCustomizedCareChecked

    private val _isWideFacilityChecked = MutableLiveData(false)
    val isWideFacilityChecked: LiveData<Boolean> = _isWideFacilityChecked

    private val _isVariousInstrumentsChecked = MutableLiveData(false)
    val isVariousInstrumentsChecked: LiveData<Boolean> = _isVariousInstrumentsChecked

    private val _isPleasantEnvironmentChecked = MutableLiveData(false)
    val isPleasantEnvironmentChecked: LiveData<Boolean> = _isPleasantEnvironmentChecked

    private val _isQuietAtmosphereChecked = MutableLiveData(false)
    val isQuietAtmosphereChecked: LiveData<Boolean> = _isQuietAtmosphereChecked


    //가격
    private val _priceRangeFormatted = MutableLiveData<Pair<String, String>>(Pair("0","15,000,000"))
    val priceRangeFormatted: LiveData<Pair<String, String>> = _priceRangeFormatted

    private val _priceRange = MutableLiveData<Pair<Float, Float>>(Pair(0f, 1500000f))
    val priceRange: LiveData<Pair<Float, Float>> = _priceRange

    val isPriceRangeAll = SingleLiveEvent<Boolean>()

    /*position, list관련*/
    //필터타입 순서 리스트
    val filterTypeOrderList = ListLiveData<String>()

    //어디서 눌러서 왔는지
    private val _currentFilterPosition = MutableLiveData<Int>()
    val currentFilterPosition: LiveData<Int> = _currentFilterPosition

    //필터 순서 총괄
    private val _filterChipList = MutableLiveData<MutableList<Pair<String, Boolean>>>()
    val filterChipList: LiveData<MutableList<Pair<String, Boolean>>> = _filterChipList

    //항목상관없이 필터링된 필터들을 담는 리스트
    private val _filteredChipList = ListLiveData<Any>()
    val filteredChipList: ListLiveData<Any> = _filteredChipList

    //필터, 검색 가능여부
    private val _isResetAndSearchValid = MutableLiveData(false)
    val isResetAndSearchValid: LiveData<Boolean> = _isResetAndSearchValid

    init {
        setFilterPosition()
        //바텀 탭레이아웃에게 그려야하는 필터순서 리스트로 전달하기 위함
        filterTypeOrderList.value = filterChipList.value?.toMap()?.keys?.toMutableList()
    }

    //홈에서 초기화누르거나 검색눌렀을때 초기화되어야함
    fun setFilterPosition() {
        //필터 순서, 필터 선택여부 동적관리 -> 여기서만 관리해주도록
        _filterChipList.value = mutableListOf(
            FilterType.TicketKind.value to (_isTicketKindFiltered.value ?: false),
            FilterType.Term.value to false,
            FilterType.Price.value to false,
            FilterType.TransactionMethod.value to (_isTransactionMethodFiltered.value ?: false),
            FilterType.AdditionalOptions.value to (_isAdditionalOptionsFiltered.value ?: false),
            FilterType.HashTag.value to (_isHashTagFiltered.value ?: false),
            FilterType.Alignment.value to false,
        )
    }

    //홈에서 필터 클릭해서 올 때 바텀에서 어떤 포지션 탭 띄워야하는지 세팅하는 곳
    fun setCurrentFilterPosition(currentFilterPosition: Int) {
        _currentFilterPosition.value = currentFilterPosition
    }

    fun filterReset() {
        hashTagCheckedList.value?.clear()
        ticketKindCheckedList.value?.clear()
        transactionMethodCheckedList.value?.clear()
        additionalOptionsCheckedList.value?.clear()
        _filteredChipList.value?.clear()
        _filteredChipList.value = _filteredChipList.value

        updateChoiceChipCheckedStatus()  //모든 초이스칩 초기화
        updateFilterChipCheckedStatus() //각 필터가 선택됐는지를 갱신
        setFilterPosition()  //홈에서 바로 초기화 누르는 경우가 있음
        updateResetAndSearchValid() //리셋버튼 갱신
    }

    private fun updateChipToFilteredChipList(chip: Any, isChecked: Boolean) {
        if (isChecked) _filteredChipList.value?.add(chip)
        else _filteredChipList.value?.remove(chip)
        _filteredChipList.value = _filteredChipList.value
        updateResetAndSearchValid()
    }

    //초기화, 검색 valid 처리
    private fun updateResetAndSearchValid() {
        _isResetAndSearchValid.value = !_filteredChipList.value.isNullOrEmpty()
    }

    /* ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ각 하위 항목 관리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ */
    fun setTicketKind(ticket: TicketKind, isChecked: Boolean = false) {
        ticketKindCheckedList.setChipState(ticket, isChecked)
        setChipSatus(ticket, isChecked)
    }

    fun setTransactionMethod(method: TransactionMethod, isChecked: Boolean = false) {
        transactionMethodCheckedList.setChipState(method, isChecked)
        setChipSatus(method, isChecked)
    }

    fun setAdditionalOptions(option: AdditionalOptions, isChecked: Boolean = false) {
        additionalOptionsCheckedList.setChipState(option, isChecked)
        setChipSatus(option, isChecked)
    }

    fun setHashTag(option: HashTag, isChecked: Boolean = false) {
        if (isChecked && hashTagCheckedList.value?.filter { it.value }?.size == 3) return
        hashTagCheckedList.setChipState(option, isChecked)
        setChipSatus(option, isChecked)
    }


    fun setPrice(min: Float, max: Float) {
        _priceRange.value = Pair(min, max)

        if (min == 0f && max == 1500000f) {  //전체 선택시
            isPriceRangeAll.value=true
        } else {
            _priceRangeFormatted.value = Pair(priceFormat(min), priceFormat(max))
            isPriceRangeAll.value=false
        }
    }

    private fun setChipSatus(type: Any, isChecked: Boolean) {
        updateChoiceChipCheckedStatus()
        updateFilterChipCheckedStatus()
        updateChipToFilteredChipList(type, isChecked)
    }


    //선택바뀔떄마다 해당 칩 하나라도 선택됐는지
    private fun updateFilterChipCheckedStatus() {
        _isTicketKindFiltered.value = ticketKindCheckedList.value?.containsValue(true)
        _isTransactionMethodFiltered.value = transactionMethodCheckedList.value?.containsValue(true)
        _isAdditionalOptionsFiltered.value = additionalOptionsCheckedList.value?.containsValue(true)
        _isHashTagFiltered.value = hashTagCheckedList.value?.containsValue(true)
    }

    private fun updateChoiceChipCheckedStatus() {

        //양도권종류
        _isGymChecked.value = choiceChipStatus(TicketKind.GYM)
        _isPtChecked.value = choiceChipStatus(TicketKind.PT)
        _isPilatesYogaChecked.value = choiceChipStatus(TicketKind.PILATES_YOGA)
        _isEtcChecked.value = choiceChipStatus(TicketKind.ETC)

        //거래방법
        _isFaceChecked.value = choiceChipStatus(TransactionMethod.FACE)
        _isNonFaceChecked.value = choiceChipStatus(TransactionMethod.NON_FACE)
        _isSellerChecked.value = choiceChipStatus(TransactionMethod.SELLER)
        _isConsumerChecked.value = choiceChipStatus(TransactionMethod.CONSUMER)

        //추가옵션
        _isShowerRoomChecked.value = choiceChipStatus(AdditionalOptions.SHOWER_ROOM)
        _isLockerRoomChecked.value = choiceChipStatus(AdditionalOptions.LOCKER_ROOM)
        _isSportWearrChecked.value = choiceChipStatus(AdditionalOptions.SPORT_WEAR)
        _isGxChecked.value = choiceChipStatus(AdditionalOptions.GX)
        _isReTransferChecked.value = choiceChipStatus(AdditionalOptions.RE_TRANSFER)
        _isRefundChecked.value = choiceChipStatus(AdditionalOptions.REFUND)
        _isHoldingChecked.value = choiceChipStatus(AdditionalOptions.HOLDING)
        _isBargainingChecked.value = choiceChipStatus(AdditionalOptions.BARGAINING)

        //해시태그
        _isKindTeacherChecked.value = choiceChipStatus(HashTag.KIND_TEACHER)
        _isSystematicLessonChecked.value = choiceChipStatus(HashTag.SYSTEMATIC_LESSON)
        _isCustomizedCareChecked.value = choiceChipStatus(HashTag.CUSTOMIZED_CARE)
        _isWideFacilityChecked.value = choiceChipStatus(HashTag.WIDE_FACILITY)
        _isVariousInstrumentsChecked.value = choiceChipStatus(HashTag.VARIOUS_INSTRUMENTS)
        _isPleasantEnvironmentChecked.value = choiceChipStatus(HashTag.PLEASANT_ENVIRONMENT)
        _isQuietAtmosphereChecked.value = choiceChipStatus(HashTag.QUIET_ATMOSPHERE)
    }

    private fun choiceChipStatus(type: Any?): Boolean? {
        return when (type) {
            is TicketKind -> if (ticketKindCheckedList.value?.containsKey(type)!!)
                ticketKindCheckedList.value?.getValue(type) else false

            is TransactionMethod -> if (transactionMethodCheckedList.value?.containsKey(type)!!)
                transactionMethodCheckedList.value?.getValue(type) else false

            is AdditionalOptions -> if (additionalOptionsCheckedList.value?.containsKey(type)!!)
                additionalOptionsCheckedList.value?.getValue(type) else false

            is HashTag -> if (hashTagCheckedList.value?.containsKey(type)!!)
                hashTagCheckedList.value?.getValue(type) else false
            else -> null
        }
    }
}