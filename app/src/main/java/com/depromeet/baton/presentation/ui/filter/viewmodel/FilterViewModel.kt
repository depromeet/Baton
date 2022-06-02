package com.depromeet.baton.presentation.ui.filter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.remote.datasource.UiState
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.domain.repository.TicketRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.filter.view.TermFragment
import com.depromeet.baton.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : BaseViewModel() {

    private val _filteredTicketCount = MutableLiveData<Int>(0)
    val filteredTicketCount: LiveData<Int> = _filteredTicketCount

    /*양도권 종류*/
    var ticketKindCheckedList = MapListLiveData<TicketKind, Boolean>()

    private val _isTicketKindFiltered = MutableLiveData<Boolean>()
    val isTicketKindFiltered: LiveData<Boolean> = _isTicketKindFiltered

    private val _isGymChecked = MutableLiveData(false)
    val isGymChecked: LiveData<Boolean> = _isGymChecked

    private val _isPtChecked = MutableLiveData(false)
    val isPtChecked: LiveData<Boolean> = _isPtChecked

    private val _isPilatesYogaChecked = MutableLiveData(false)
    val isPilatesYogaChecked: LiveData<Boolean> = _isPilatesYogaChecked

    private val _isEtcChecked = MutableLiveData(false)
    val isEtcChecked: LiveData<Boolean> = _isEtcChecked

    //피티만 선택
    private val _isOnlyPtChecked = MutableLiveData(false)
    val isOnlyPtChecked: LiveData<Boolean> = _isOnlyPtChecked

    //헬스만 선택
    private val _isOnlyGymChecked = MutableLiveData(false)
    val isOnlyGymChecked: LiveData<Boolean> = _isOnlyGymChecked

    /*거래방법*/
    private val _isTransactionMethodFiltered = MutableLiveData<Boolean>()

    var tradeTypeCheckedList = MapListLiveData<TradeType, Boolean>()

    private val _isFaceChecked = MutableLiveData(false)
    val isFaceChecked: LiveData<Boolean> = _isFaceChecked

    private val _isNonFaceChecked = MutableLiveData(false)
    val isNonFaceChecked: LiveData<Boolean> = _isNonFaceChecked

    /*거래비*/
    var transferFeeCheckedList = MapListLiveData<TransferFee, Boolean>()

    private val _isSellerChecked = MutableLiveData(false)
    val isSellerChecked: LiveData<Boolean> = _isSellerChecked

    private val _isConsumerChecked = MutableLiveData(false)
    val isConsumerChecked: LiveData<Boolean> = _isConsumerChecked

    private val _isNaChecked = MutableLiveData(false)
    val isNaChecked: LiveData<Boolean> = _isNaChecked


    /*추가옵션*/
    private var additionalOptionsCheckedList = MapListLiveData<AdditionalOptions, Boolean>()

    private val _isAdditionalOptionsFiltered = MutableLiveData<Boolean>()

    private val _isShowerRoomChecked = MutableLiveData(false)
    val isShowerRoomChecked: LiveData<Boolean> = _isShowerRoomChecked

    private val _isLockerRoomChecked = MutableLiveData(false)
    val isLockerRoomChecked: LiveData<Boolean> = _isLockerRoomChecked

    private val _isSportWearChecked = MutableLiveData(false)
    val isSportWearChecked: LiveData<Boolean> = _isSportWearChecked

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

    private val _isNewInstrumentsChecked = MutableLiveData(false)
    val isNewInstrumentsChecked: LiveData<Boolean> = _isNewInstrumentsChecked

    private val _isCrowdedChecked = MutableLiveData(false)
    val isCrowdedChecked: LiveData<Boolean> = _isCrowdedChecked

    private val _isFewPeopleChecked = MutableLiveData(false)
    val isFewPeopleChecked: LiveData<Boolean> = _isFewPeopleChecked

    private val _isPleasantEnvironmentChecked = MutableLiveData(false)
    val isPleasantEnvironmentChecked: LiveData<Boolean> = _isPleasantEnvironmentChecked

    private val _isQuietAtmosphereChecked = MutableLiveData(false)
    val isQuietAtmosphereChecked: LiveData<Boolean> = _isQuietAtmosphereChecked

    private val _isStationAreaChecked = MutableLiveData(false)
    val isStationAreaChecked: LiveData<Boolean> = _isStationAreaChecked


    //피티 기간
    private val _ptTermRange = MutableLiveData(Pair(0f, 60f))
    val ptTermRange: LiveData<Pair<Float, Float>> = _ptTermRange

    private val _ptTermRangeFormatted = MutableLiveData(Pair("0", "60"))
    val ptTermRangeFormatted: LiveData<Pair<String, String>> = _ptTermRangeFormatted

    private val _isPtTermFiltered = MutableLiveData<Boolean>()
    val isPtTermFiltered: LiveData<Boolean> = _isPtTermFiltered


    //헬스 기간
    private val _gymTermRange = MutableLiveData(Pair(0f, 12f))
    val gymTermRange: LiveData<Pair<Float, Float>> = _gymTermRange

    private val _gymTermRangeFormatted = MutableLiveData(Pair("0", "12"))
    val gymTermRangeFormatted: LiveData<Pair<String, String>> = _gymTermRangeFormatted

    private val _isGymTermFiltered = MutableLiveData<Boolean>(false)
    val isGymTermFiltered: LiveData<Boolean> = _isGymTermFiltered

    private val _isTermRangeFiltered = MutableLiveData<Boolean>()

    //가격
    private val _priceRangeFormatted = MutableLiveData(Pair("0", "15,000,000"))
    val priceRangeFormatted: LiveData<Pair<String, String>> = _priceRangeFormatted

    private val _priceRange = MutableLiveData(Pair(0f, 1500000f))
    val priceRange: LiveData<Pair<Float, Float>> = _priceRange

    private val _isPriceFiltered = MutableLiveData<Boolean>()
    val isPriceFiltered: LiveData<Boolean> = _isPriceFiltered

    private val _priceReset = SingleLiveEvent<Any>()
    val priceReset: LiveData<Any> = _priceReset

    //정렬
    private val _alignmentCheckedOption = MutableLiveData<Alignment>()
    val alignmentCheckedOption: LiveData<Alignment> = _alignmentCheckedOption

/*    private val _isNearDistanceChecked = MutableLiveData(false)
    val isNearDistanceChecked: LiveData<Boolean> = _isNearDistanceChecked

    private val _isLowPriceChecked = MutableLiveData(false)
    val isLowPriceChecked: LiveData<Boolean> = _isLowPriceChecked

    private val _isPopularChecked = MutableLiveData(false)
    val isPopularChecked: LiveData<Boolean> = _isPopularChecked

    private val _isManyTermChecked = MutableLiveData(false)
    val isManyTermChecked: LiveData<Boolean> = _isManyTermChecked*/

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

    //리셋 클릭
    private val _isResetClick = MutableLiveData(false)
    val isResetClick: LiveData<Boolean> = _isResetClick

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ필터들 순서 총괄ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
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
            FilterType.Term.value to (_isTermRangeFiltered.value ?: false),
            FilterType.Price.value to (_isPriceFiltered.value ?: false),
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

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ필터링된 항목들 리스트에 add, removeㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/

    //밑에 필터링된 칩들만 주루룩 보이게
    private fun updateChipToFilteredChipList(chip: Any? = null, isChecked: Boolean? = null) {
        val chipStr = chip.toString()
        if (chip != null && isChecked != null) {
            if (isChecked) {
                if ((chipStr.contains("회"))) {
                    _filteredChipList.value?.let { _filteredChipList.value?.removeAll(it.filter { it.toString().contains("회") }) }
                }
                if ((chipStr.contains("개월"))) {
                    _filteredChipList.value?.let { _filteredChipList.value?.removeAll(it.filter { it.toString().contains("개월") }) }
                }
                if ((chipStr.contains("원"))) {
                    _filteredChipList.value?.let { _filteredChipList.value?.removeAll(it.filter { it.toString().contains("원") }) }
                }
                _filteredChipList.value?.add(chip)
            } else _filteredChipList.value?.remove(chip)

            updateFilteredChipListToChip()
        }
    }

    private fun updateFilteredChipListToChip() {
        //피티 이용권이 없는데 필라테스 || 요가 || 선택한 개수가 2개 이상아님 =>회수를 리스트에서 지움
        if (_filteredChipList.value?.contains(TicketKind.PT) == false &&
            _filteredChipList.value?.contains(TicketKind.PILATES_YOGA) == false &&
            _filteredChipList.value?.contains(TicketKind.ETC) == false &&
            ticketKindCheckedList.value?.filter { it.value }?.size!! < 2
        ) {
            _filteredChipList.value?.toMutableList()?.let { _filteredChipList.value?.removeAll(it.filter { it.toString().contains("회") }) }
            _isPtTermFiltered.value = false
        }
        //헬스 이용권이 없는데 필라테스 || 요가 || 선택한 개수가 2개 이상아님 =>회수를 리스트에서 지움
        if (_filteredChipList.value?.contains(TicketKind.HEALTH) == false &&
            _filteredChipList.value?.contains(TicketKind.PILATES_YOGA) == false &&
            _filteredChipList.value?.contains(TicketKind.ETC) == false &&
            ticketKindCheckedList.value?.filter { it.value }?.size!! < 2
        ) {
            _filteredChipList.value?.toMutableList()?.let { _filteredChipList.value?.removeAll(it.filter { it.toString().contains("개월") }) }
            _isGymTermFiltered.value = false
        }
        _filteredChipList.value = _filteredChipList.value
    }


    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ필터 전체 리셋 관리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
    fun filterReset() {
        _isResetClick.value = true

        //가격 초기화
        _priceRange.value = Pair(TermFragment.MIN, TermFragment.PRICE_MAX)
        _isPriceFiltered.value = false

        //피티 횟수 초기화
        setPtTerm(TermFragment.MIN, TermFragment.PT_MAX)
        _isOnlyPtChecked.value = false

        //헬스 기간 초기화
        setGymTerm(TermFragment.MIN, TermFragment.GYM_MAX)
        _isOnlyGymChecked.value = false

        hashTagCheckedList.value?.clear()
        ticketKindCheckedList.value?.clear()
        tradeTypeCheckedList.value?.clear()
        transferFeeCheckedList.value?.clear()
        additionalOptionsCheckedList.value?.clear()
        _filteredChipList.value?.clear()
        _filteredChipList.value = _filteredChipList.value

        updateAllStatus()
    }

    fun setResetClickFalse() {
        _isResetClick.value = false
    }

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ칩 하위 항목 관리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
    fun setTicketKind(ticket: TicketKind, isChecked: Boolean = false) {
        ticketKindCheckedList.setChipCheckedStatus(ticket, isChecked)
        updateAllStatus(ticket, isChecked)


        //양도권 종류에 따른 기간 레이아웃 분기 업데이트
        updatePtTermStatusFromTicketChoice()
        updateGymTermStatusFromTicketChoice()
        updateEtcTermStatusFromTicketChoice()
    }

    fun setTradeType(method: TradeType, isChecked: Boolean = false) {
        if (isChecked && tradeTypeCheckedList.value?.filter { it.value }?.size == 1) return
        tradeTypeCheckedList.setChipCheckedStatus(method, isChecked)
        updateAllStatus(method, isChecked)
    }

    fun setTransferFeeType(who: TransferFee, isChecked: Boolean = false) {
        if (isChecked && transferFeeCheckedList.value?.filter { it.value }?.size == 1) return
        transferFeeCheckedList.setChipCheckedStatus(who, isChecked)
        updateAllStatus(who, isChecked)
    }

    fun setAdditionalOptions(option: AdditionalOptions, isChecked: Boolean = false) {
        additionalOptionsCheckedList.setChipCheckedStatus(option, isChecked)
        updateAllStatus(option, isChecked)
    }

    fun setHashTag(tag: HashTag, isChecked: Boolean = false) {
        if (isChecked && hashTagCheckedList.value?.filter { it.value }?.size == 3) return
        hashTagCheckedList.setChipCheckedStatus(tag, isChecked)
        updateAllStatus(tag, isChecked)
    }

    fun setAlignment(standard: Alignment) {
        _alignmentCheckedOption.value = standard
        updateFilteredTicketCount()
    }

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ슬라이더 하위 항목 관리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ양도권 종류 선택에 따른 기간 슬라이더 분기
    //피티만 선택시
    private fun updatePtTermStatusFromTicketChoice() {
        _isOnlyPtChecked.value = _isPtChecked.value == true
                && _isGymChecked.value == false
                && _isPilatesYogaChecked.value == false
                && _isEtcChecked.value == false
    }


    //헬스만 선택시
    private fun updateGymTermStatusFromTicketChoice() {
        _isOnlyGymChecked.value = _isGymChecked.value == true
                && _isPtChecked.value == false
                && _isPilatesYogaChecked.value == false
                && _isEtcChecked.value == false
    }

    //그외
    private fun updateEtcTermStatusFromTicketChoice() {
        if ((ticketKindCheckedList.value?.filter { it.value }?.size ?: 0) >= 2
            || _isPilatesYogaChecked.value == true || _isEtcChecked.value == true
        ) {
            _isOnlyPtChecked.value = true
            _isOnlyGymChecked.value = true
        }
    }

    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡPT: 횟수
    fun setPtTerm(min: Float, max: Float) {
        _ptTermRange.value = Pair(min, max)
        if (min == TermFragment.MIN && max == TermFragment.PT_MAX) {  //전체 선택시
            _isPtTermFiltered.value = false //필터링 안됨
            updateAllStatus(_ptTermRangeFormatted.value!!.first + "회~" + _ptTermRangeFormatted.value!!.second + "회", false) //하단 리스트에서 삭제
        } else {
            _ptTermRangeFormatted.value = Pair(ceilAndToStringFormat(min), ceilAndToStringFormat(max))
            updateAllStatus(_ptTermRangeFormatted.value!!.first + "회~" + _ptTermRangeFormatted.value!!.second + "회", true) //하단 리스트에 추가
            _isPtTermFiltered.value = true //필터링 됨
        }
    }

    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ헬스: 기간
    fun setGymTerm(min: Float, max: Float) {
        _gymTermRange.value = Pair(min, max)
        if (min == TermFragment.MIN && max == TermFragment.GYM_MAX) {  //전체 선택시
            _isGymTermFiltered.value = false //필터링 안됨
            updateAllStatus(_gymTermRangeFormatted.value!!.first + "개월~" + _gymTermRangeFormatted.value!!.second + "개월", false) //하단 리스트에서 삭제
        } else {
            _gymTermRangeFormatted.value = Pair(ceilAndToStringFormat(min), ceilAndToStringFormat(max))
            updateAllStatus(_gymTermRangeFormatted.value!!.first + "개월~" + _gymTermRangeFormatted.value!!.second + "개월", true) //하단 리스트에 추가
            _isGymTermFiltered.value = true //필터링 됨
        }
    }

    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ가격
    fun setPrice(min: Float, max: Float) {
        _priceRange.value = Pair(min, max)
        if (min == TermFragment.MIN && max == TermFragment.PRICE_MAX) {  //전체 선택시
            _isPriceFiltered.value = false
            updateAllStatus(_priceRangeFormatted.value!!.first + "원~" + _priceRangeFormatted.value!!.second + "원", false)
        } else {
            _isPriceFiltered.value = true
            _priceRangeFormatted.value = Pair(priceFormat(min), priceFormat(max))
            updateAllStatus(_priceRangeFormatted.value!!.first + "원~" + _priceRangeFormatted.value!!.second + "원", true)
        }
    }

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ초이스칩 / 필터칩 / 선택된 필터 리스트 업데이트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
    // 초이스칩 / 필터칩 / 필터링된 칩 리스트 업데이트
    private fun updateAllStatus(type: Any? = null, isChecked: Boolean? = null) {
        updateFilteredTicketCount()
        updateChoiceChipCheckedStatus()  //각 초이스칩 상태 업데이트
        updateFilterChipCheckedStatus()  //각 필터칩 상태 업데이트
        updateChipToFilteredChipList(type, isChecked) //필터링된 칩리스트 업데이트
        updateResetAndSearchValid()  //리셋버튼 상태 업데이트
        setFilterPosition()  //홈에서 바로 초기화 누르는 경우가 있음
    }

    //초기화, 검색 valid 처리  ->필터링된 칩 리스트가 없거나/슬라이드가 전부 전체로 되어있지 않은 경우 초기화 가능
    private fun updateResetAndSearchValid() {
        _isResetAndSearchValid.value = !_filteredChipList.value.isNullOrEmpty()
    }

    // 필터칩 상태 업데이트  =>  선택바뀔떄마다 해당 칩 하나라도 선택됐는지=>   각 리스트들 item 중 true가 하나도 없으면 필터링 안됨
    private fun updateFilterChipCheckedStatus() {
        _isTicketKindFiltered.value = ticketKindCheckedList.value?.containsValue(true)
        _isTransactionMethodFiltered.value = tradeTypeCheckedList.value?.containsValue(true) == true || transferFeeCheckedList.value?.containsValue(true) == true
        _isAdditionalOptionsFiltered.value = additionalOptionsCheckedList.value?.containsValue(true)
        _isHashTagFiltered.value = hashTagCheckedList.value?.containsValue(true)
        _isTermRangeFiltered.value = _isPtTermFiltered.value == true || _isGymTermFiltered.value == true
    }

    // 초이스칩 상태 업데이트  =>  선택바뀔떄마다 해당 칩 하나라도 선택됐는지=>   각 리스트들 item 중 true가 하나도 없으면 필터링 안됨
    private fun updateChoiceChipCheckedStatus() {
        //양도권종류
        _isGymChecked.value = choiceChipStatus(TicketKind.HEALTH)
        _isPtChecked.value = choiceChipStatus(TicketKind.PT)
        _isPilatesYogaChecked.value = choiceChipStatus(TicketKind.PILATES_YOGA)
        _isEtcChecked.value = choiceChipStatus(TicketKind.ETC)

        //거래방법
        _isFaceChecked.value = choiceChipStatus(TradeType.CONTECT)
        _isNonFaceChecked.value = choiceChipStatus(TradeType.UNTECT)

        //거래비
        _isSellerChecked.value = choiceChipStatus(TransferFee.SELLER)
        _isConsumerChecked.value = choiceChipStatus(TransferFee.CONSUMER)
        _isNaChecked.value = choiceChipStatus(TransferFee.NONE)

        //추가옵션
        _isShowerRoomChecked.value = choiceChipStatus(AdditionalOptions.SHOWER_ROOM)
        _isLockerRoomChecked.value = choiceChipStatus(AdditionalOptions.LOCKER_ROOM)
        _isSportWearChecked.value = choiceChipStatus(AdditionalOptions.SPORT_WEAR)
        _isGxChecked.value = choiceChipStatus(AdditionalOptions.GX)
        _isReTransferChecked.value = choiceChipStatus(AdditionalOptions.RE_TRANSFER)
        _isRefundChecked.value = choiceChipStatus(AdditionalOptions.REFUND)
        _isHoldingChecked.value = choiceChipStatus(AdditionalOptions.HOLDING)
        _isBargainingChecked.value = choiceChipStatus(AdditionalOptions.BARGAINING)

        //해시태그
        _isKindTeacherChecked.value = choiceChipStatus(HashTag.KIND_TEACHER)
        _isSystematicLessonChecked.value = choiceChipStatus(HashTag.SYSTEMATIC_CLASS)
        _isCustomizedCareChecked.value = choiceChipStatus(HashTag.CUSTOMIZED_CARE)
        _isWideFacilityChecked.value = choiceChipStatus(HashTag.SPACIOUS_FACILITIES)
        _isVariousInstrumentsChecked.value = choiceChipStatus(HashTag.VARIOUS_EQUIPMENT)
        _isNewInstrumentsChecked.value = choiceChipStatus(HashTag.NEW_EQUIPMENT)
        _isCrowdedChecked.value = choiceChipStatus(HashTag.MANY_PEOPLE)
        _isFewPeopleChecked.value = choiceChipStatus(HashTag.LESS_PEOPLE)
        _isPleasantEnvironmentChecked.value = choiceChipStatus(HashTag.AGREEMENT)
        _isQuietAtmosphereChecked.value = choiceChipStatus(HashTag.QUIET_AMBIENCE)
        _isStationAreaChecked.value = choiceChipStatus(HashTag.STATION_AREA)
    }

    private fun choiceChipStatus(type: Any?): Boolean? {
        return when (type) {
            is TicketKind -> if (ticketKindCheckedList.value?.containsKey(type)!!)
                ticketKindCheckedList.value?.getValue(type) else false

            is TradeType -> if (tradeTypeCheckedList.value?.containsKey(type)!!)
                tradeTypeCheckedList.value?.getValue(type) else false

            is TransferFee -> if (transferFeeCheckedList.value?.containsKey(type)!!)
                transferFeeCheckedList.value?.getValue(type) else false

            is AdditionalOptions -> if (additionalOptionsCheckedList.value?.containsKey(type)!!)
                additionalOptionsCheckedList.value?.getValue(type) else false

            is HashTag -> if (hashTagCheckedList.value?.containsKey(type)!!)
                hashTagCheckedList.value?.getValue(type) else false

            else -> null
        }
    }

    /** ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡAPIㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
    fun updateFilteredTicketCount() {
        viewModelScope.launch {
            kotlin.runCatching {
                Log.e(
                    "ㅡㅡㅡㅡㅡㅡㅡ콜렉ㅡㅡㅡㅡㅡ",
                    "해시태그:" + "${hashTagCheckedList.value?.map { it.key.toString() }}\n" +
                            "latitude:" + "경도\n" +
                            "longitude:" + "위도\n" +
                            "minPrice:" + "${_gymTermRange.value?.first?.toInt()}\n" +
                            "maxPrice:" + "${priceRange.value?.second?.toInt()}\n" +
                            "minRemainNumber:" + "${_gymTermRange.value?.first?.toInt()}\n" +
                            "maxRemainNumber:" + "${_ptTermRange.value?.second?.toInt()}\n" +
                            "minRemainMonth:" + "${_gymTermRange.value?.first?.toInt()}\n" +
                            "maxRemainMonth:" + "${_gymTermRange.value?.second?.toInt()}\n" +
                            "maxDistance:" + "${500}\n" +
                            "ticketTypes:" + "${ticketKindCheckedList.value?.map { it.key.toString() }}\n" +
                            "ticketTradeType:" + "${tradeTypeCheckedList.value?.keys.toString()}\n" +
                            "transferFee:" + "${transferFeeCheckedList.value?.keys.toString()}\n" +
                            "alignment" + "${alignmentCheckedOption.value}\n" +
                            "hasClothes:" + "${isSportWearChecked.value}\n" +
                            "hasLocker:" + "${isLockerRoomChecked.value}\n" +
                            "hasShower:" + "${isShowerRoomChecked.value}\n" +
                            "hasGx:" + "${isGxChecked.value}\n" +
                            "canResell:" + "${isReTransferChecked.value}\n" +
                            "canRefund:" + "${isRefundChecked.value}\n" +
                            "isHold:" + "${isHoldingChecked.value}\n" +
                            "canNego:" + "${isBargainingChecked.value}\n"


                )
                ticketRepository.getFilteredTicketCount(
                    page = 1,
                    size = 4,
                    //place: String?,
                    hashtag = hashTagCheckedList.value?.map { it.key.toString() },
                    latitude = 36.1234f,
                    longitude = 127.1234f,
                    //town: String?,
                    minPrice = priceRange.value?.first?.toInt(),
                    maxPrice = priceRange.value?.second?.toInt(),
                    minRemainNumber = _ptTermRange.value?.first?.toInt(),
                    maxRemainNumber = _ptTermRange.value?.second?.toInt(),
                    minRemainMonth = _gymTermRange.value?.first?.toInt(),
                    maxRemainMonth = _gymTermRange.value?.second?.toInt(),
                    maxDistance = 500,
                    ticketTypes = ticketKindCheckedList.value?.map { it.key.toString() },
                    ticketTradeType = tradeTypeCheckedList.value?.keys.toString(),
                    transferFee = transferFeeCheckedList.value?.keys.toString(),
                    // ticketState : String ?,
                    sortType = alignmentCheckedOption.value?.toString(),
                    hasClothes = isSportWearChecked.value,
                    hasLocker = isLockerRoomChecked.value,
                    hasShower = isShowerRoomChecked.value,
                    hasGx = isGxChecked.value,
                    canResell = isReTransferChecked.value,
                    canRefund = isRefundChecked.value,
                    isHold = isHoldingChecked.value,
                    canNego = isBargainingChecked.value,
                    //isMembership = isLockerRoomChecked.value,
                ).collect { UiState ->
                    when (UiState) {
                        is UiState.Success<*> -> {
                            _filteredTicketCount.value = UiState.data as Int
                        }
                        is UiState.Error -> {
                            UiState.error?.message?.let { errorMessage ->
                                Timber.e(errorMessage)
                            }
                        }

                    }
                }
            }
        }
    }
}
