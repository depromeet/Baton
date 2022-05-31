package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.domain.model.AdditionalOptions
import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.domain.model.TradeType
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.ui.detail.model.MarketBasicInfo
import com.depromeet.baton.presentation.ui.detail.model.Seller
import com.depromeet.baton.presentation.ui.detail.model.TicketInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor() : BaseViewModel() {

    //추후 변경
    val _uiState = MutableLiveData<UIState?>(UIState.HasData)
    val uiState: LiveData<UIState?> get() = _uiState

    val _marketInfoState= MutableLiveData<MarketBasicInfo>()
    val marketInfoState : LiveData<MarketBasicInfo> get() = _marketInfoState

    val _ticketState= MutableLiveData<TicketInfo>( )
    val ticketState : LiveData<TicketInfo> get() = _ticketState

    init {
        val sellerData = Seller("바통","바통","","2022.04.20")

        _marketInfoState.value =MarketBasicInfo(
            sellerData,
            gymName = "휴메이크 휘트니스 석촌점",
            price = "200,000원",
            canSuggest = true,
            registration_date = "22.05.02"+"가입",
            views = "260",
            likes="30",
            url = "",
            detailContent = "헬스 회원권  60일권  200,000원으로 저렴하게 양도합니다!\u2028- 접근성 좋음 주변에 버스터미널, 정류장 등 가까움\n" +
                    "- 선생님도 친절/대회 수상이력 당연O, 몸 컨디션 체크 등등 설명도 정말 잘해주세요!!\n" +
                    "- PT시간은 4시이후, 저녁시간대 가능",
            transactionMethod = TradeType.FACE,
            moreTag = arrayListOf(AdditionalOptions.BARGAINING, AdditionalOptions.HOLDING)

        )

        _ticketState.value=TicketInfo(
            ticketKind = TicketKind.GYM,
            effectiveDate = "22.12.31",
            price = "200,000원",
            transferCoast = "10,000원",
            infoTag =  arrayListOf(HashTag.SYSTEMATIC_LESSON,HashTag.CUSTOMIZED_CARE, HashTag.QUIET_ATMOSPHERE)

        )
    }

   //dummy data
    fun loadData(){

    }

    val POSSIBLE ="가격 제안 가능"
    val IMPOSSIBLE="가격제안 불가능"
}