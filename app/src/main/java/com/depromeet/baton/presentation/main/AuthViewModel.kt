package com.depromeet.baton.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.api.user.TokenApi
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userinfoRepository: UserinfoRepository
) : ViewModel() {

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Loading)
    val tokenState = _tokenState.asStateFlow()

    fun authValidation(){

        viewModelScope.launch {
            userinfoRepository.authValidation(authRepository.authInfo?.accessToken!!,authRepository.authInfo?.refreshToken!!).let{
                when(it){
                    is TokenApi.RefreshResult.Success ->{
                        authRepository.setAuthInfo(it.response.access_token!!,it.response.refresh_token!!)
                        _tokenState.value = TokenState.Available
                        Timber.e("auth token refresh success")
                    }
                    is TokenApi.RefreshResult.Failure-> {
                        _tokenState.value = TokenState.Expiration
                        authRepository.logout()
                    }
                }
            }
            _tokenState.value = TokenState.Loading
        }
    }

}


sealed class TokenState{
    object Available : TokenState()
    object Expiration : TokenState()
    object Loading : TokenState()
}