package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMylocationDetailBinding
import com.depromeet.baton.domain.repository.AccountRepository
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.remote.user.UserAddressResponse
import com.depromeet.baton.util.BatonSpfManager
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyLocationDetailActivity : BaseActivity<ActivityMylocationDetailBinding>(R.layout.activity_mylocation_detail) {
    @Inject lateinit var spfManager: BatonSpfManager
    private val viewmodel by viewModels<MyDetailLocationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setListener()
        setObserver()
    }

    private fun setObserver(){
        viewmodel.networkState
            .flowWithLifecycle(lifecycle)
            .onEach { net -> Timber.e(net.data.toString())
                when(net){
                    is NetworkResult.Loading ->{}
                    is NetworkResult.Success -> {
                        nextView()
                    }
                    is NetworkResult.Error ->{this.BdsToast("주소 정보 저장에 실패했습니다").show()}
                }
            }.launchIn(lifecycleScope)

        viewmodel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { binding.uistate =it }
            .launchIn(lifecycleScope)
    }

    private fun initView(){
        binding.roadAddressTv.text= spfManager.getAddress().roadAddress
        binding.addressTv.text = spfManager.getAddress().address
        binding.detailAddressToolbar.titleTv.text ="상세 주소 입력"
    }

    private fun setListener(){
        binding.detailAddressNextBtn.setOnClickListener {
           viewmodel.updateAddress( binding.addressDetailEt.getText())
        }

        binding.detailAddressToolbar.backBtn.setOnClickListener {
            onBackPressed()
        }

        KeyboardVisibilityEvent.setEventListener(this) {
            binding.addressDetailEt.searchBarKeyBoardListener(it)
            binding.detailAddressNextBtn.isEnabled = !it
        }
    }

    private fun nextView(){
        spfManager.saveDetailAddress( binding.addressDetailEt.getText())
        this.BdsToast("위치 정보가 저장되었습니다", binding.detailAddressNextBtn.top).show()
        val intent = Intent(this, AddressActivity::class.java)
        intent.putExtra("isChanged",true)
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}

@HiltViewModel
class MyDetailLocationViewModel @Inject constructor(
    private val userInfoRepository: AccountRepository,
    private val authRepository: AuthRepository,
    private val spfManager: BatonSpfManager
) :BaseViewModel(){

    private val _networkState = MutableStateFlow<NetworkResult<UserAddressResponse>>(NetworkResult.Loading())
    val networkState =_networkState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState(btnisEnable = true, isLoading = false))
    val uiState = _uiState.asStateFlow()

    fun updateAddress( detail : String){
        viewModelScope.launch {
            _uiState.update { it.copy(btnisEnable = false, isLoading = true) }
            val userId = authRepository.authInfo!!.userId // TODO authinfo 변경 필요
            val res = userInfoRepository.updateAddress(userId,spfManager.getMyLatitude(), spfManager.getMyLongitude(),spfManager.getAddress().roadAddress,detail)
            res.run{
                _networkState.update { res }
                when(res){
                    is NetworkResult.Error ->{
                        Timber.e(res.message)
                        _uiState.update { it.copy(btnisEnable = false, isLoading = false) }
                    }
                    is NetworkResult.Success->{
                        _uiState.update { it.copy(btnisEnable = true, isLoading = false) }
                    }
                 }
             }
           }
        }


    data class UiState(
        val btnisEnable :Boolean,
        val isLoading : Boolean
    )

}