package com.depromeet.baton.presentation.ui.address.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.depromeet.baton.map.domain.usecase.SearchAddressUseCase
import com.depromeet.baton.map.domain.usecase.SearchItem
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.ui.address.model.AddressInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SearchAddressViewModel @Inject constructor(
    private val searchAddressUseCase :SearchAddressUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){


    //검색 입력 필드
    private val _editSearchText = MutableLiveData<String>("")
    val editSearchText: LiveData<String>
        get() = _editSearchText

    //검색 연관어 아이템
    private val _items  = MutableStateFlow<List<AddressInfo>>(ArrayList<AddressInfo>())
    val items : StateFlow<List<AddressInfo>>
        get() = _items


   private val _uiState = MutableLiveData<UIState>(UIState.Init)
    val uiState : LiveData<UIState> get() = _uiState



    init{}

    fun searchAddress(query : String){
        _editSearchText.value=query

        if( _editSearchText.value!=""){
            viewModelScope.launch {
                searchAddressUseCase.searchAddress(query ).collect{
                    when(it){
                        // content가 있음
                        is SearchItem.Content ->{
                            _items.value = it.data!!.map{ entity -> AddressInfo(entity)}
                            _uiState.value= (UIState.HasData)
                        }
                        is SearchItem.Empty -> {
                            _uiState.value= (UIState.NoData)
                        }
                    }
                }
            }
        }else{
            _uiState.value= (UIState.Init)
        }
    }

}