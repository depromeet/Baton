package com.depromeet.baton.presentation.ui.writepost.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Url
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
) : BaseViewModel() {
    //작성중인 포지션
    private val _currentLevel = MutableLiveData(1)
    val currentLevel: LiveData<Int> = _currentLevel

    //장소검색
    private val _uiState = MutableLiveData<UIState>(UIState.Init)
    val uiState: LiveData<UIState> get() = _uiState

    //선택한 장소
    private val _selectedShopInfo = MutableLiveData<ShopInfo>()
    val selectedShopInfo: LiveData<ShopInfo> = _selectedShopInfo

    //선택 완료 이벤트
    private val _isShopSelected = SingleLiveEvent<Any>()
    val isShopSelected: LiveData<Any> = _isShopSelected

    //검색 결과 리스트
    private val _shopInfoList = MutableStateFlow<List<ShopInfo>>(ArrayList())
    val shopInfoList: StateFlow<List<ShopInfo>> = _shopInfoList.asStateFlow()

    //선택한 이미지 리스트
    private val _selectedPhotoList = MutableLiveData<MutableList<Uri>>()
    val selectedPhotoList: LiveData<MutableList<Uri>> = _selectedPhotoList

    //글자 수 저장
    private val _currentTextLength = MutableLiveData(0)
    val currentTextLength: LiveData<Int> = _currentTextLength

    fun setNextLevel(nextLevel: Boolean = true) {
        if (nextLevel) { //다음버튼
            when (_currentLevel.value) {
                1 -> nextLevelEvent(GO_TO_TICKET_INFO)
                2 -> nextLevelEvent(GO_TO_TRANSACTION_METHOD)
                3 -> nextLevelEvent(GO_TO_DESCRIPTION)
            }
            _currentLevel.value = _currentLevel.value?.plus(1)
        } else {
            _currentLevel.value = _currentLevel.value?.minus(1)
        }
    }

    //viewEvent관련
    private fun nextLevelEvent(level: Int) = viewEvent(level)

    fun searchPlace(query: String) {
        _shopInfoList.value = listOf(
            ShopInfo("투엑스 휘트니스 대치점", "서울 강남구 삼성로123"),
            ShopInfo("투게더 휘트니스 양평점", "서울 영등포구 선유로141"),
            ShopInfo("투웨이 필라테스 당산점", "서울 강동구 선유로323"),
            ShopInfo("투스데이 헬스 개봉점", "서울 관악구 개봉로331"),
        )
        viewModelScope.launch {
            runCatching {
                //TODO 데이터 collect
            }.onSuccess {
                /*        searchShopUseCase.searchShop(query ).collect{
                            when(it){
                                is ShopItem.Content ->{
                                }
                                is ShopItem.Empty -> {
                                }
                            }
                        }*/
            }.onFailure {
                _uiState.value = (UIState.Init)
                Timber.e(it.toString())
            }
        }
    }

    fun setSelectShop(shopInfo: ShopInfo) {
        _selectedShopInfo.value = shopInfo
        _isShopSelected.call()
    }

    fun setCurrentTextLength(length: Int) {
        _currentTextLength.value = length
    }

    fun setSelectedPhotoList(photoList: MutableList<Uri>) {
        _selectedPhotoList.value = photoList
    }

    companion object {
        const val GO_TO_TICKET_INFO = 2
        const val GO_TO_TRANSACTION_METHOD = 3
        const val GO_TO_DESCRIPTION = 4
        const val SHOP_SELECTED = 5
    }
}
