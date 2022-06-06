package com.depromeet.baton.presentation.ui.writepost.viewmodel

import android.net.Uri
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.request.RequestTicketPost
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.domain.repository.SearchRepository
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.map.domain.usecase.SearchItem
import com.depromeet.baton.map.domain.usecase.SearchShopUseCase
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.util.MapListLiveData
import com.depromeet.baton.presentation.util.SingleLiveEvent
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    private val searchShopUseCase: SearchShopUseCase,
    private val searchRepository: SearchRepository,
    private val spfManager: BatonSpfManager
) : BaseViewModel() {
    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ포지션별 버튼 상태
    private val _isNextBtnEnable = MutableLiveData(false)
    val isNextBtnEnable: LiveData<Boolean> = _isNextBtnEnable

    private val _isLevelOneNextBtnEnable = MutableLiveData(false)
    private val _isLevelTwoNextBtnEnable = MutableLiveData(false)
    private val _isLevelThreeNextBtnEnable = MutableLiveData(false)
    private val _isLevelFourNextBtnEnable = MutableLiveData(false)

    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡUI상태
    //현재 작성 레벨
    private val _writePostPositionViewEvents: MutableStateFlow<List<WritePostPositionViewEvent>> = MutableStateFlow(emptyList())
    val writePostPositionViewEvents = _writePostPositionViewEvents.asStateFlow()

    //검색결과 UI 상태
    private val _shopSearchUiState = MutableLiveData<UIState>(UIState.Init)
    val shopSearchUiState: LiveData<UIState> = _shopSearchUiState

    //장소 등록 바텀
    private val _bottomSearchUiState: MutableStateFlow<BottomSearchUiState> = MutableStateFlow(createBottomSearchState())
    val bottomSearchUiState = _bottomSearchUiState.asStateFlow()

    private val _bottomSearchViewEvents: MutableStateFlow<List<BottomSearchViewEvent>> = MutableStateFlow(emptyList())
    val bottomSearchViewEvents = _bottomSearchViewEvents.asStateFlow()

    //장소 직접 입력
    private val _selfWriteUiState: MutableStateFlow<SelfWriteUiState> = MutableStateFlow(createSelfWriteState())
    val selfWriteUiState = _selfWriteUiState.asStateFlow()

    private val _selfWriteViewEvents: MutableStateFlow<List<SelfWriteViewEvent>> = MutableStateFlow(emptyList())
    val selfWriteViewEvents = _selfWriteViewEvents.asStateFlow()

    //멤버십 정보
    private val _membershipInfoUiState: MutableStateFlow<MembershipInfoUiState> = MutableStateFlow(createMembershipInfoState())
    val membershipInfoUiState = _membershipInfoUiState.asStateFlow()

    //판매방식
    private val _transactionMethodUiState: MutableStateFlow<TransactionMethodUiState> = MutableStateFlow(createTransactionMethodState())
    val transactionMethodUiState = _transactionMethodUiState.asStateFlow()

    private val _transactionMethodViewEvents: MutableStateFlow<List<TransactionMethodViewEvent>> = MutableStateFlow(emptyList())
    val transactionMethodViewEvents = _transactionMethodViewEvents.asStateFlow()

    //설명글
    private val _descriptionUiState: MutableStateFlow<DescriptionUiState> = MutableStateFlow(createDescriptionState())
    val descriptionUiState = _descriptionUiState.asStateFlow()


    //todo 그외 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //작성중인 포지션
    private val _currentLevel = MutableLiveData(1)
    val currentLevel: LiveData<Int> = _currentLevel

    //선택한 장소
    private val _selectedShopInfo = MutableLiveData<ShopInfo>()
    val selectedShopInfo: LiveData<ShopInfo> = _selectedShopInfo

    //선택 완료 이벤트->다이어로그 닫힘
    private val _isShopSelected = SingleLiveEvent<Any>()
    val isShopSelected: LiveData<Any> = _isShopSelected

    //검색 결과 리스트
    private val _shopInfoList = MutableStateFlow<ArrayList<ShopEntity>>(ArrayList())
    val shopInfoList: StateFlow<ArrayList<ShopEntity>> = _shopInfoList

    //선택한 이미지 리스트
    private val _selectedPhotoList = MutableLiveData<MutableList<Uri>>()
    val selectedPhotoList: LiveData<MutableList<Uri>> = _selectedPhotoList

    //글자 수 저장
    private val _currentTextLength = MutableLiveData(0)
    val currentTextLength: LiveData<Int> = _currentTextLength

    /*해시태그*/
    private var hashTagCheckedList = MapListLiveData<HashTag, Boolean>()

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

    /*양도권 종류*/
    var ticketKindCheckedList = MapListLiveData<TicketKind, Boolean>()

    private val _isGymChecked = MutableLiveData(false)
    val isGymChecked: LiveData<Boolean> = _isGymChecked

    private val _isPtChecked = MutableLiveData(false)
    val isPtChecked: LiveData<Boolean> = _isPtChecked

    private val _isPilatesYogaChecked = MutableLiveData(false)
    val isPilatesYogaChecked: LiveData<Boolean> = _isPilatesYogaChecked

    private val _isEtcChecked = MutableLiveData(false)
    val isEtcChecked: LiveData<Boolean> = _isEtcChecked

    //기간제 횟수제
    private val _isPeriodChecked = MutableLiveData(false)
    val isPeriodChecked: LiveData<Boolean> = _isPeriodChecked

    private val _isNumberChecked = MutableLiveData(false)
    val isNumberChecked: LiveData<Boolean> = _isNumberChecked

    /*추가옵션*/
    private var additionalOptionsCheckedList = MapListLiveData<AdditionalOptions, Boolean>()

    private val _isBargainingChecked = MutableLiveData(false)
    val isBargainingChecked: LiveData<Boolean> = _isBargainingChecked

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

    /*거래방법*/
    var tradeTypeCheckedList = MapListLiveData<TradeType, Boolean>()

    private val _isFaceChecked = MutableLiveData(false)
    val isFaceChecked: LiveData<Boolean> = _isFaceChecked

    private val _isNonFaceChecked = MutableLiveData(false)
    val isNonFaceChecked: LiveData<Boolean> = _isNonFaceChecked

    private val _isBothChecked = MutableLiveData(false)
    val isBothChecked: LiveData<Boolean> = _isBothChecked

    /*거래비*/
    var transferFeeCheckedList = MapListLiveData<TransferFee, Boolean>()

    private val _isSellerChecked = MutableLiveData(false)
    val isSellerChecked: LiveData<Boolean> = _isSellerChecked

    private val _isConsumerChecked = MutableLiveData(false)
    val isConsumerChecked: LiveData<Boolean> = _isConsumerChecked

    private val _isNaChecked = MutableLiveData(false)
    val isNaChecked: LiveData<Boolean> = _isNaChecked

    //todo ㅡㅡㅡㅡㅡㅡ화면이동ㅡㅡㅡㅡㅡㅡ
    private fun writePositionAddViewEvent(viewEvent: WritePostPositionViewEvent) {
        _writePostPositionViewEvents.update { it + viewEvent }
    }

    fun writePositionConsumeViewEvent(viewEvent: WritePostPositionViewEvent) {
        _writePostPositionViewEvents.update { it - viewEvent }
    }

    sealed interface WritePostPositionViewEvent {
        object GoMembershipInfo : WritePostPositionViewEvent
        object GoTransactionMethod : WritePostPositionViewEvent
        object GoDescription : WritePostPositionViewEvent
        object GoDone : WritePostPositionViewEvent
    }

    fun setNextLevel(nextLevel: Boolean = true) {
        if (nextLevel) { //다음버튼
            when (_currentLevel.value) {
                1 -> writePositionAddViewEvent(WritePostPositionViewEvent.GoMembershipInfo)
                2 -> writePositionAddViewEvent(WritePostPositionViewEvent.GoTransactionMethod)
                3 -> writePositionAddViewEvent(WritePostPositionViewEvent.GoDescription)
                4 -> writePositionAddViewEvent(WritePostPositionViewEvent.GoDone)
            }
            _currentLevel.value = _currentLevel.value?.plus(1)
        } else {
            _currentLevel.value = _currentLevel.value?.minus(1)
        }
    }

    //todo ㅡㅡㅡㅡㅡㅡ버튼 상태ㅡㅡㅡㅡㅡㅡ
    fun setNextLevelEnable() {
        when (_currentLevel.value) {
            1 -> _isNextBtnEnable.value = _isLevelOneNextBtnEnable.value
            2 -> _isNextBtnEnable.value = _isLevelTwoNextBtnEnable.value
            3 -> _isNextBtnEnable.value = _isLevelThreeNextBtnEnable.value
            4 -> _isNextBtnEnable.value = _isLevelFourNextBtnEnable.value
        }
    }


    //todo ㅡㅡㅡㅡㅡㅡ장소 등록 바텀
    private fun bottomSearchAddViewEvent(viewEvent: BottomSearchViewEvent) {
        _bottomSearchViewEvents.update { it + viewEvent }
    }

    fun bottomSearchConsumeViewEvent(viewEvent: BottomSearchViewEvent) {
        _bottomSearchViewEvents.update { it - viewEvent }
    }

    sealed interface BottomSearchViewEvent {
        object ToSearchShop : BottomSearchViewEvent
        object ToSelfWrite : BottomSearchViewEvent
        object SelfWriteDone : BottomSearchViewEvent
    }

    data class BottomSearchUiState(
        val onGoSearchShopClick: () -> Unit,
        val onGoSelfWriteClick: () -> Unit,
        val setBottomDialogDismiss: () -> Unit,
    )

    private fun createBottomSearchState(): BottomSearchUiState {
        return BottomSearchUiState(
            onGoSearchShopClick = ::handleGoSearchShopClick,
            onGoSelfWriteClick = ::handleGoSelfWriteClick,
            setBottomDialogDismiss = ::handleDialogDismissClick,
        )
    }

    private fun handleGoSearchShopClick() {
        bottomSearchAddViewEvent(BottomSearchViewEvent.ToSearchShop)
    }

    private fun handleGoSelfWriteClick() {
        bottomSearchAddViewEvent(BottomSearchViewEvent.ToSelfWrite)
    }

    private fun handleDialogDismissClick() {
        bottomSearchAddViewEvent(BottomSearchViewEvent.SelfWriteDone)
    }

    //todo ㅡㅡㅡㅡㅡㅡ1. 주소 직접 입력ㅡㅡㅡㅡㅡㅡ
    private fun selfWriteAddViewEvent(viewEvent: SelfWriteViewEvent) {
        _selfWriteViewEvents.update { it + viewEvent }
    }

    fun selfWriteConsumeViewEvent(viewEvent: SelfWriteViewEvent) {
        _selfWriteViewEvents.update { it - viewEvent }
    }

    data class SelfWriteUiState(
        val center: String,
        val centerName: String,
        val detailAddress: String,
        val citySelected: String,
        val regionSelected: String,
        val onCenterNameChanged: (Editable?) -> Unit,
        val onCenterChanged: (Editable?) -> Unit,
        val onCitySelected: (Int?) -> Unit,
        val onRegionSelected: (Int?) -> Unit,
        val onDetailAddressChanged: (Editable?) -> Unit,
        val onSelfWriteAddressDoneClick: () -> Unit,
    ) {

        private val isCenterValid = center.isNotBlank()
        private val isCenterNameValid = centerName.isNotBlank()
        private val isDetailAddressValid = detailAddress.isNotBlank()
        private val isCityValid = citySelected.isNotBlank()
        private val isRegionValid = regionSelected.isNotBlank()

        val isEnabled = isCenterValid && isCenterNameValid && isDetailAddressValid && isCityValid && isRegionValid
    }

    private fun createSelfWriteState(): SelfWriteUiState {
        return SelfWriteUiState(
            center = "",
            centerName = "",
            detailAddress = "",
            citySelected = "",
            regionSelected = "",
            onCenterNameChanged = ::handleCenterNameChanged,
            onCenterChanged = ::handleCenterChanged,
            onCitySelected = ::handleCitySelected,
            onRegionSelected = ::handleRegionSelected,
            onDetailAddressChanged = ::handleDetailAddressChanged,
            onSelfWriteAddressDoneClick = ::handleSelfWriteAddressDoneClick
        )
    }

    sealed interface SelfWriteViewEvent {
        object SelfWriteDone : SelfWriteViewEvent
    }

    private fun handleCenterNameChanged(editable: Editable?) {
        _selfWriteUiState.update { it.copy(center = editable.toString()) }
    }

    private fun handleCenterChanged(editable: Editable?) {
        _selfWriteUiState.update { it.copy(centerName = editable.toString()) }
    }

    private fun handleDetailAddressChanged(editable: Editable?) {
        _selfWriteUiState.update { it.copy(detailAddress = editable.toString()) }
    }

    private fun handleCitySelected(position: Int?) {
        _selfWriteUiState.update { it.copy(citySelected = position.toString()) }
    }

    private fun handleRegionSelected(position: Int?) {
        _selfWriteUiState.update { it.copy(regionSelected = position.toString()) }
    }

    private fun handleSelfWriteAddressDoneClick() {
        selfWriteAddViewEvent(SelfWriteViewEvent.SelfWriteDone)
    }


    //위치 검색
    fun searchPlace(query: String) {
        if (query == "") {
            _shopSearchUiState.value = UIState.Init
            return
        }
        viewModelScope.launch {
            runCatching {
            }.onSuccess {
                searchShopUseCase.searchShop(query).collect {
                    when (it) {
                        is SearchItem.Content -> {
                            _shopSearchUiState.value = UIState.HasData
                            _shopInfoList.value = it.data?.filterNot { it.name.contains("당구") || it.name.contains("골프") || it.name.contains("볼링") || it.name.contains("탁구") } as ArrayList<ShopEntity>
                        }
                        is SearchItem.Empty -> {
                            _shopSearchUiState.value = UIState.NoData
                        }
                    }
                }
            }.onFailure {
                _shopSearchUiState.value = (UIState.Init)
                Timber.e(it.toString())
            }
        }
    }

    //위치 선택 완료
    fun setSelectShop(shopInfo: ShopInfo) {
        _selectedShopInfo.value = shopInfo
        _isShopSelected.call()
        _isLevelOneNextBtnEnable.value = true
        setNextLevelEnable()
    }

    //이미지 리스트
    fun setSelectedPhotoList(photoList: MutableList<Uri>) {
        _selectedPhotoList.value?.clear()
        _selectedPhotoList.value = photoList
    }

    //사진 지우기
    fun deleteImg(position: Int) {
        _selectedPhotoList.value?.removeAt(position)
        _selectedPhotoList.value = _selectedPhotoList.value
    }

    //해시태그 선택
    fun setHashTag(tag: HashTag, isChecked: Boolean = false) {
        hashTagCheckedList.setChipCheckedStatus(tag, isChecked)
        updateChoiceChipCheckedStatus()
    }


    //todo ㅡㅡㅡㅡㅡㅡ2.회원권 정보ㅡㅡㅡㅡㅡㅡ

    data class MembershipInfoUiState(
        val termChanged: String,
        val priceChanged: String,
        val onTermChanged: (Editable) -> Unit,
        val onPriceChanged: (Editable) -> Unit,
        val onChipChecked: (Any, Boolean) -> Unit,
    )

    private fun createMembershipInfoState(): MembershipInfoUiState {
        return MembershipInfoUiState(
            termChanged = "",
            priceChanged = "",
            onTermChanged = ::handleTermDetailChanged,
            onPriceChanged = ::handlePriceChanged,
            onChipChecked = ::handleChipChanged,
        )
    }

    private fun handleTermDetailChanged(editable: Editable?) {
        _membershipInfoUiState.update { it.copy(termChanged = editable.toString()) }
        setLevelTwoNextBtnEnable()
    }

    private fun handlePriceChanged(editable: Editable?) {
        _membershipInfoUiState.update { it.copy(priceChanged = editable.toString()) }
        setLevelTwoNextBtnEnable()
    }

    fun handleChipChanged(any: Any, isChecked: Boolean) {
        when (any) {
            is TicketKind -> setTicketKind(any, isChecked)
            is AdditionalOptions -> setAdditionalOptions(any, isChecked)
            is Term -> setTerm(any, isChecked)
            is TradeType -> setTradeType(any, isChecked)
            is TransferFee -> setTransferFee(any, isChecked)
        }
    }

    private fun setTicketKind(ticket: TicketKind, isChecked: Boolean) {
        if (isChecked) ticketKindCheckedList.value?.clear()
        ticketKindCheckedList.setChipCheckedStatus(ticket, isChecked)
        updateChoiceChipCheckedStatus()
        setLevelTwoNextBtnEnable()
    }

    private fun setTerm(kind: Term, isChecked: Boolean) {
        //기간, 횟수 하나만 선택 가능
        _isPeriodChecked.value = false
        _isNumberChecked.value = false
        when (kind) {
            Term.PERIOD -> _isPeriodChecked.value = isChecked
            Term.NUMBER -> _isNumberChecked.value = isChecked
        }
        setLevelTwoNextBtnEnable()
    }

    private fun setAdditionalOptions(option: AdditionalOptions, isChecked: Boolean) {
        additionalOptionsCheckedList.setChipCheckedStatus(option, isChecked)
        updateChoiceChipCheckedStatus()
    }

    private fun setLevelTwoNextBtnEnable() {
        _isLevelTwoNextBtnEnable.value =
            ticketKindCheckedList.value?.containsValue(true) == true
                    && (_isPeriodChecked.value == true || _isNumberChecked.value == true)
                    && _membershipInfoUiState.value.termChanged.isNotEmpty()
                    && _membershipInfoUiState.value.priceChanged.isNotEmpty()

        setNextLevelEnable()
    }

    //todo ㅡㅡㅡㅡㅡㅡ3. 판매방식 선택ㅡㅡㅡㅡㅡㅡ
    sealed interface TransactionMethodViewEvent {
        object ShowToolTip : TransactionMethodViewEvent
    }

    data class TransactionMethodUiState(
        val onToolTipClick: () -> Unit,
        val onChipChecked: (TradeType, Boolean) -> Unit,
    )

    private fun createTransactionMethodState(): TransactionMethodUiState {
        return TransactionMethodUiState(
            onToolTipClick = ::handleToolTipClick,
            onChipChecked = ::handleChipChanged,
        )
    }

    private fun transactionMethodAddViewEvent(viewEvent: TransactionMethodViewEvent) {
        _transactionMethodViewEvents.update { it + viewEvent }
    }

    fun transactionMethodConsumeViewEvent(viewEvent: TransactionMethodViewEvent) {
        _transactionMethodViewEvents.update { it - viewEvent }
    }

    private fun handleToolTipClick() {
        transactionMethodAddViewEvent(TransactionMethodViewEvent.ShowToolTip)
    }

    private fun setTradeType(method: TradeType, isChecked: Boolean = false) {
        if (isChecked) tradeTypeCheckedList.value?.clear()
        tradeTypeCheckedList.setChipCheckedStatus(method, isChecked)
        updateChoiceChipCheckedStatus()

        setLevelThreeNextBtnEnable()
    }

    private fun setTransferFee(who: TransferFee, isChecked: Boolean = false) {
        if (isChecked) transferFeeCheckedList.value?.clear()
        transferFeeCheckedList.setChipCheckedStatus(who, isChecked)
        updateChoiceChipCheckedStatus()

        setLevelThreeNextBtnEnable()
    }

    private fun setLevelThreeNextBtnEnable() {
        _isLevelThreeNextBtnEnable.value =
            tradeTypeCheckedList.value?.containsValue(true) == true
                    && transferFeeCheckedList.value?.containsValue(true) == true

        setNextLevelEnable()
    }
    //todo ㅡㅡㅡㅡㅡㅡ4. 설명글 작성ㅡㅡㅡㅡㅡㅡ

    //글자수 실시간
    fun setCurrentTextLength(length: Int) {
        _currentTextLength.value = length
    }

    data class DescriptionUiState(
        val descriptionChanged: String,
        val onDescriptionChanged: (Editable) -> Unit,
    )

    private fun createDescriptionState(): DescriptionUiState {
        return DescriptionUiState(
            descriptionChanged = "",
            onDescriptionChanged = ::handleDescriptionChanged,
        )
    }

    private fun handleDescriptionChanged(editable: Editable?) {
        _descriptionUiState.update { it.copy(descriptionChanged = editable.toString()) }
        _currentTextLength.value = editable.toString().length

        if (_currentTextLength.value != 0) _isLevelFourNextBtnEnable.value = true

        setNextLevelEnable()
    }

    //todo ㅡㅡㅡㅡㅡㅡㅡㅡ태그들

    private fun updateChoiceChipCheckedStatus() {
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

        //양도권 종류
        _isGymChecked.value = choiceChipStatus(TicketKind.HEALTH)
        _isPtChecked.value = choiceChipStatus(TicketKind.PT)
        _isPilatesYogaChecked.value = choiceChipStatus(TicketKind.PILATES_YOGA)
        _isEtcChecked.value = choiceChipStatus(TicketKind.ETC)

        //추가옵션
        _isShowerRoomChecked.value = choiceChipStatus(AdditionalOptions.SHOWER_ROOM)
        _isLockerRoomChecked.value = choiceChipStatus(AdditionalOptions.LOCKER_ROOM)
        _isSportWearChecked.value = choiceChipStatus(AdditionalOptions.SPORT_WEAR)
        _isGxChecked.value = choiceChipStatus(AdditionalOptions.GX)
        _isReTransferChecked.value = choiceChipStatus(AdditionalOptions.RE_TRANSFER)
        _isRefundChecked.value = choiceChipStatus(AdditionalOptions.REFUND)
        _isBargainingChecked.value = choiceChipStatus(AdditionalOptions.BARGAINING)

        //거래방법
        _isFaceChecked.value = choiceChipStatus(TradeType.CONTECT)
        _isNonFaceChecked.value = choiceChipStatus(TradeType.UNTECT)
        _isBothChecked.value = choiceChipStatus(TradeType.BOTH)

        //거래비
        _isSellerChecked.value = choiceChipStatus(TransferFee.SELLER)
        _isConsumerChecked.value = choiceChipStatus(TransferFee.CONSUMER)
        _isNaChecked.value = choiceChipStatus(TransferFee.NONE)
    }

    private fun choiceChipStatus(type: Any?): Boolean? {
        return when (type) {
            is HashTag -> if (hashTagCheckedList.value?.containsKey(type)!!)
                hashTagCheckedList.value?.getValue(type) else false

            is TicketKind -> if (ticketKindCheckedList.value?.containsKey(type)!!)
                ticketKindCheckedList.value?.getValue(type) else false

            is AdditionalOptions -> if (additionalOptionsCheckedList.value?.containsKey(type)!!)
                additionalOptionsCheckedList.value?.getValue(type) else false

            is TradeType -> if (tradeTypeCheckedList.value?.containsKey(type)!!)
                tradeTypeCheckedList.value?.getValue(type) else false

            is TransferFee -> if (transferFeeCheckedList.value?.containsKey(type)!!)
                transferFeeCheckedList.value?.getValue(type) else false

            else -> true
        }
    }


    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡapi 콜ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    fun postTicket() {
        val body = RequestTicketPost(
            location = _selectedShopInfo.value?.shopName ?: "",
            address = _selectedShopInfo.value?.shopAddress ?: "",
            price = 30000,
            // expiryDate,
            type = ticketKindCheckedList.value?.filter { it.value }!!.map { it.key.value }[0],
            tradeType = tradeTypeCheckedList.value?.filter { it.value }!!.map { it.key.value }[0],
            transferFee = transferFeeCheckedList.value?.filter { it.value }!!.map { it.key.value }[0],
            canNego = _isNaChecked.value!!,
            hasShower = isShowerRoomChecked.value!!,
            hasLocker = isLockerRoomChecked.value!!,
            hasClothes = isSportWearChecked.value!!,
            hasGx = _isGxChecked.value!!,
            canResell = _isReTransferChecked.value!!,
            canRefund = _isRefundChecked.value!!,
            description = descriptionUiState.value.descriptionChanged,
            isMembership = isPeriodChecked.value!!,
            isHolding = true, //하아
            remainingNumber = 6, //남은 횟수. isMembership이 False면 필수
            latitude = spfManager.getLocation().latitude.toFloat(),
            longitude = spfManager.getLocation().longitude.toFloat(),
            tags = hashTagCheckedList.value!!.map { it.key.value }
        ).toRequestBody()

        viewModelScope.launch {
            runCatching { searchRepository.postTicket(body, null) }
                .onSuccess {
                   Log.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ","${it}")
                }
                .onFailure { }
        }
    }
}


