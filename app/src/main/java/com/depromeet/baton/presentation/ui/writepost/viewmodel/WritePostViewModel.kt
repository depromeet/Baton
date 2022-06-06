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
import com.depromeet.baton.presentation.ui.writepost.view.SelfWriteAddressUiState
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
    private val _isNextBtnEnable = MutableLiveData(false)
    val isNextBtnEnable: LiveData<Boolean> = _isNextBtnEnable

    private val _isLevelOneNextBtnEnable = MutableLiveData(false)
    private val _isLevelTwoNextBtnEnable = MutableLiveData(false)
    private val _isLevelThreeNextBtnEnable = MutableLiveData(false)
    private val _isLevelFourNextBtnEnable = MutableLiveData(false)

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

    //검색결과 UI 상태
    private val _shopSearchUiState = MutableLiveData<UIState>(UIState.Init)
    val shopSearchUiState: LiveData<UIState> = _shopSearchUiState

    //검색결과 없을 때 직접입력 UI상태
    private val _selfWriteAddressUiState: MutableStateFlow<SelfWriteAddressUiState> = MutableStateFlow(createSelfWriteAddressState())
    val selfWriteAddressUiState = _selfWriteAddressUiState.asStateFlow()

    //선택한 이미지 리스트
    private val _selectedPhotoList = MutableLiveData<MutableList<Uri>>()
    val selectedPhotoList: LiveData<MutableList<Uri>> = _selectedPhotoList

    //선택한 이미지 리스트
    private val _selectedPhotoOriginList = MutableLiveData<MutableList<Uri>>()
    val selectedPhotoOriginList: LiveData<MutableList<Uri>> = _selectedPhotoOriginList

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


    //todo 뷰이벤트
    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()


    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    //화면 이동
    fun setNextLevel(nextLevel: Boolean = true) {
        if (nextLevel) { //다음버튼
            when (_currentLevel.value) {
                1 -> viewEvent(GO_TO_MEMBERSHIP_INFO)
                2 -> viewEvent(GO_TO_TRANSACTION_METHOD)
                3 -> viewEvent(GO_TO_DESCRIPTION)
                4 -> viewEvent(GO_TO_DONE)
            }
            _currentLevel.value = _currentLevel.value?.plus(1)
        } else {
            _currentLevel.value = _currentLevel.value?.minus(1)
        }
    }

    //각 뷰 다음버튼 이너블
    fun setNextLevelEnable() {
        when (_currentLevel.value) {
            1 -> _isNextBtnEnable.value = _isLevelOneNextBtnEnable.value
            2 -> _isNextBtnEnable.value = _isLevelTwoNextBtnEnable.value
            3 -> _isNextBtnEnable.value = _isLevelThreeNextBtnEnable.value
            4 -> _isNextBtnEnable.value = _isLevelFourNextBtnEnable.value
        }
    }

    //todo ㅡㅡㅡㅡㅡㅡ1. 장소 선택ㅡㅡㅡㅡㅡㅡ

    //주소 직접 입렭
    private fun createSelfWriteAddressState(): SelfWriteAddressUiState {
        return SelfWriteAddressUiState(
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

    private fun handleCenterNameChanged(editable: Editable?) {
        _selfWriteAddressUiState.update { it.copy(center = editable.toString()) }
    }

    private fun handleCenterChanged(editable: Editable?) {
        _selfWriteAddressUiState.update { it.copy(centerName = editable.toString()) }
    }

    private fun handleDetailAddressChanged(editable: Editable?) {
        _selfWriteAddressUiState.update { it.copy(detailAddress = editable.toString()) }
    }

    private fun handleCitySelected(position: Int?) {
        _selfWriteAddressUiState.update { it.copy(citySelected = position.toString()) }
    }

    private fun handleRegionSelected(position: Int?) {
        _selfWriteAddressUiState.update { it.copy(regionSelected = position.toString()) }
    }

    private fun handleSelfWriteAddressDoneClick() {
        addViewEvent(ViewEvent.SelfWriteAddressDone)
    }

    //위치 선택
    fun setSearchShopPosition(position: String) {
        when (position) {
            SEARCH_SHOP -> viewEvent(SEARCH_SHOP)
            SELF_WRITE -> viewEvent(SELF_WRITE)
            DIALOG_DISMISS -> viewEvent(DIALOG_DISMISS)
        }
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

    fun deleteImg(position: Int) {
        Log.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ","${selectedPhotoList.value?.size}")
        Log.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ","${position}")
      //  if(_selectedPhotoList.value?.size!=0)
      _selectedPhotoList.value?.removeAt(position)
        Log.e("ㅡㅡㅡㅡㅡㅡ디움ㅡㅡㅡㅡㅡㅡㅡㅡ","${selectedPhotoList.value}")
        _selectedPhotoList.value =       _selectedPhotoList.value
    }

    //해시태그 선택
    fun setHashTag(tag: HashTag, isChecked: Boolean = false) {
        hashTagCheckedList.setChipCheckedStatus(tag, isChecked)
        updateChoiceChipCheckedStatus()
    }


    //todo ㅡㅡㅡㅡㅡㅡ2.회원권 정보ㅡㅡㅡㅡㅡㅡ

    //티켓종류 해시태그

    //기간 횟수 해시태그

    //남은기간 입력 텍스트 필드

    //가격 택스트 필드

    //가격 제안가능 토클

    //추가옵션 토클

    //todo ㅡㅡㅡㅡㅡㅡ3. 판매방식 선택ㅡㅡㅡㅡㅡㅡ

    //판매방식 해시태그

    //양도비 토글

    //todo ㅡㅡㅡㅡㅡㅡ4. 설명글 작성ㅡㅡㅡㅡㅡㅡ

    //글자수 실시간
    fun setCurrentTextLength(length: Int) {
        _currentTextLength.value = length
    }


    sealed interface ViewEvent {
        object SelfWriteAddressDone : ViewEvent
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
    }

    private fun choiceChipStatus(type: Any?): Boolean? {
        return when (type) {
            is HashTag -> if (hashTagCheckedList.value?.containsKey(type)!!)
                hashTagCheckedList.value?.getValue(type) else false

            else -> null
        }
    }


    //todo ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡapi 콜ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
/*    val location: String,
    val address: String,
    val price: Int,
    val expiryDate: String,
    val type: String,
    val tradeType: String,
    val transferFee: Int,
    val canNego: Boolean,
    val hasShower: Boolean,
    val hasLocker: Boolean,
    val hasClothes: Boolean,
    val hasGx: Boolean,
    val canResell: Boolean,
    val canRefund: Boolean,
    val description: String,
    val isMembership: Boolean,
    val isHolding: Boolean,
    val remainingNumber: Int,
    val latitude: Float,
    val longitude: Float,
    val tags: List<String>,*/

    fun postTicket() {
/*        val body = RequestTicketPost(
            location = _selectedShopInfo.value?.shopName ?: "",
            address = _selectedShopInfo.value?.shopAddress ?: "",
            price,
            expiryDate,
            type,
            tradeType,
            transferFee,
            canNego,
            hasShower,
            hasLocker,
            hasClothes,
            hasGx,
            canResell,
            canRefund,
            description,
            isMembership,
            isHolding,
            remainingNumber,
            latitude = spfManager.getLocation().latitude.toFloat(),
            longitude = spfManager.getLocation().longitude.toFloat(),
            tags
        ).toRequestBody()*/
        /*    val location: String,
            val address: String,
            val price: Int,
            val expiryDate: String,
            val type: String,
            val tradeType: String,
            val transferFee: Int,
            val canNego: Boolean,
            val hasShower: Boolean,
            val hasLocker: Boolean,
            val hasClothes: Boolean,
            val hasGx: Boolean,
            val canResell: Boolean,
            val canRefund: Boolean,
            val description: String,
            val isMembership: Boolean,
            val isHolding: Boolean,
            val remainingNumber: Int,
            val latitude: Float,
            val longitude: Float,
            val tags: List<String>,*/
        /*       viewModelScope.launch {
                   runCatching { searchRepository.postTicket(body, null) }
                       .onSuccess {
                      //     _makeCardSuccess.value = true
                       }
                       .onFailure { Timber.e("카드너 작성 실패 : ${it.message}") }
               }*/
    }


    companion object {
        const val GO_TO_MEMBERSHIP_INFO = 2
        const val GO_TO_TRANSACTION_METHOD = 3
        const val GO_TO_DESCRIPTION = 4
        const val GO_TO_DONE = 5

        const val SEARCH_SHOP = "SEARCH_SHOP"
        const val SELF_WRITE = "SELF_WRITE"
        const val DIALOG_DISMISS = "DIALOG_DISMISS"
    }
}

