package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.util.Event
import com.depromeet.baton.presentation.util.SingleLiveEvent
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.applyConnectionSpec
import timber.log.Timber

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val context : Context = application


    private val _profileImageUri = MutableLiveData<Uri>()
    val profileImageUri : LiveData<Uri> get() = _profileImageUri


    private val _isChangedImage = MutableLiveData<Boolean>(false)
    val isChangedImage : LiveData<Boolean> get()= _isChangedImage

    init{
        _profileImageUri.value = uriConverter(ProfileIcon.Happy.size56)
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

    fun onClickEmotion(image: ProfileIcon){
        _profileImageUri.value= uriConverter(image.size56)
    }

    fun setImage(image: Image){
        _profileImageUri.value = image.uri
    }

    fun submitProfile(){
        //Bottom 에서 확인 눌렀을 때
        _isChangedImage.value= true
    }

    private fun uriConverter(id: Int):Uri{
        val resourceId = id

        val uriParser= Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(resourceId))
            .appendPath(context.resources.getResourceTypeName(resourceId))
            .appendPath(context.resources.getResourceEntryName(resourceId))
            .build()

        return uriParser
    }


}