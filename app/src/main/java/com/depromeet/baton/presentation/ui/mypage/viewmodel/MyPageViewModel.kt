package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.bds.R
import com.nguyenhoanglam.imagepicker.model.Image
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val context : Context = application

    private val _uiState: MutableStateFlow<MypageUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    init {
        //APi 호출 구간
    }
    private fun createState(): MypageUiState {
        return MypageUiState(
            nickName = "김이박바통",
            phoneNumber = "01000000000",
            joinDate="2020.02.02",
            profileImage = uriConverter(context, R.drawable.ic_img_profile_startled_56)
        )
    }

    fun updateNickname(nickName: String ){
        _uiState.update { it.copy(nickName = nickName) }
    }

    fun updateProfileImg(profileImage: Uri){
        _uiState.update { it.copy(profileImage= profileImage) }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }


    sealed interface ViewEvent {

    }

}
data class MypageUiState(
    val nickName: String,
    val phoneNumber: String,
    val joinDate: String,
    val profileImage: Uri,
) {

}