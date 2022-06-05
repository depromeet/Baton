package com.depromeet.baton.presentation.ui.writepost.viewmodel

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.map.domain.usecase.SearchItem
import com.depromeet.baton.map.domain.usecase.SearchShopUseCase
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
<<<<<<< HEAD
import com.depromeet.baton.presentation.ui.writepost.view.BottomSearchContainerFragment
import com.depromeet.baton.presentation.util.Event
=======
import com.depromeet.baton.presentation.ui.sign.AddAccountViewModel
import com.depromeet.baton.presentation.ui.writepost.view.SelfWriteAddressUiState
>>>>>>> dabin/home-filter
import com.depromeet.baton.presentation.util.SingleLiveEvent
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
    private val searchShopUseCase: SearchShopUseCase
) : BaseViewModel() {
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
    private val _selfWriteAddressUiState: MutableStateFlow<SelfWriteAddressUiState> = MutableStateFlow(createState())
    val selfWriteAddressUiState = _selfWriteAddressUiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()


    //선택한 이미지 리스트
    private val _selectedPhotoList = MutableLiveData<MutableList<Uri>>()
    val selectedPhotoList: LiveData<MutableList<Uri>> = _selectedPhotoList

    //글자 수 저장
    private val _currentTextLength = MutableLiveData(0)
    val currentTextLength: LiveData<Int> = _currentTextLength

    private val _searchShopPositionEvent = MutableLiveData<Event<String>>()
    val searchShopPositionEvent: LiveData<Event<String>> = _searchShopPositionEvent

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

    fun setSearchShopPosition(position: String) {
        when (position) {
            SEARCH_SHOP -> viewEvent(SEARCH_SHOP)
            SELF_WRITE -> viewEvent(SELF_WRITE)
            DIALOG_DISMISS -> viewEvent(DIALOG_DISMISS)
        }
    }

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
                            _shopInfoList.value = it.data?.filterNot { it.name.contains("당구") || it.name.contains("골프") || it.name.contains("볼링") } as ArrayList<ShopEntity>
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

    fun setSelectShop(shopInfo: ShopInfo) {
        _selectedShopInfo.value = shopInfo
        _isShopSelected.call()
    }

    private fun createState(): SelfWriteAddressUiState {
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

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    fun setCurrentTextLength(length: Int) {
        _currentTextLength.value = length
    }

    fun setSelectedPhotoList(photoList: MutableList<Uri>) {
        _selectedPhotoList.value = photoList
    }


    sealed interface ViewEvent {
        object SelfWriteAddressDone : ViewEvent
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

    private val _selfWriteViewEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val selfWriteViewEvent: LiveData<Any> = _selfWriteViewEvent

    private val _searchShopBackClickViewEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val searchShopBackClickViewEvent: LiveData<Any> = _searchShopBackClickViewEvent

}

