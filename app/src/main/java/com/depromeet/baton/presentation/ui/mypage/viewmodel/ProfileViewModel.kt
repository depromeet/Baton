package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.util.Event
import com.depromeet.baton.presentation.util.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel: BaseViewModel() {
    private val _profileStatus= MutableLiveData<ProfileIcon>()
    val profileStatus: LiveData<ProfileIcon>  get() =_profileStatus

    private val _isChangedImage = MutableLiveData<Boolean>(false)
    val isChangedImage : LiveData<Boolean> get()= _isChangedImage

    init{
        _profileStatus.value = ProfileIcon.Happy
    }

    fun setImageStatus(){
        /** 1.현재 image 상태를 받는다
         * 2. image stauts 설정
         * 3. click Event 마다 status 설정
         * 4. 확인 버튼을 누를 때 server 전송
         *
         */
        viewModelScope.launch {

        }

    }

    fun onClickImage( image: ProfileIcon){
        _profileStatus.value = image
        Timber.e("onClickImage ${image.name}")
    }

    fun submitProfile(){
        //Bottom 에서 확인 눌렀을 때
        _isChangedImage.value= true
        Timber.e("submit ${_profileStatus.value?.name}/ ${isChangedImage.value}")

    }


}