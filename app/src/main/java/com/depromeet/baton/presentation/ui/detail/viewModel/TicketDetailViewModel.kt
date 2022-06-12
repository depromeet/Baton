package com.depromeet.baton.presentation.ui.detail.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.TicketInfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.ui.detail.model.*
import com.depromeet.baton.presentation.util.dateFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    application: Application,
    private val spfManager: BatonSpfManager,
    private val authRepository: AuthRepository,
    private val ticketInfoRepository: TicketInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {


    private val context: Context = application

    //추후 변경
    private val _ticketState = MutableLiveData<DetailTicketInfoUiState>()
    val ticketState get() = _ticketState

    private val _viewEvents: MutableStateFlow<List<DetailViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _netWorkState = MutableStateFlow<TicketDetailNetWork>(TicketDetailNetWork.Loading)
    val netWorkState = _netWorkState.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        _netWorkState.update { TicketDetailNetWork.Loading }
        viewModelScope.launch {
            //API 호출
            runCatching {
                val ticketId = savedStateHandle.get<Int>("ticketId")!!
                val res =  ticketInfoRepository.getTicketInfo(
                    ticketId = ticketId,
                    spfManager.getMyLongitude(),
                    spfManager.getMyLatitude()
                )
                when(res){
                    is NetworkResult.Error ->{
                        Timber.e(res.message)
                        _netWorkState.update { TicketDetailNetWork.Failure(res.message.toString()) }
                    }
                    is NetworkResult.Success->{
                        if (res.data != null) {
                            val ticket = res.data!!
                            val tempUserId = 1  //TODO userID 변경 authRepository.authInfo?.userId
                            val tempSellerId = ticket.seller.id
                            val state = DetailTicketInfoUiState(
                                DetailTicketInfo(
                                    ticketId = ticket.id,
                                    ticketType = TicketKind.valueOf(ticket.type),
                                    seller = DetailTicketInfo.Seller(
                                        tempSellerId,
                                        ticket.seller.nickname,
                                        ticket.seller.createdOn
                                    ),
                                    isOwner = tempUserId == tempSellerId,
                                    detailUrl = "https://map.naver.com/v5/search/${ticket.location}",
                                    mapUrl = "http://map.naver.com/index.nhn?slng=${spfManager.getMyLongitude()}&slat=${spfManager.getMyLatitude()}" +
                                            "&stext=내 위치&elng=${ticket!!.longitude}&elat=${ticket!!.latitude}" +
                                            "&pathType=3&showMap=true&etext=${ticket!!.location}&menu=route",
                                    emptyIcon = initEmptyIcon(TicketKind.PILATES_YOGA),
                                    location = DetailLocationInfo(
                                        location = ticket.location,
                                        ticket.address,
                                        ticket.longitude,
                                        ticket.latitude,
                                        ticket.distance.toFloat()
                                    ),
                                    createdDate = dateFormatUtil(ticket.createAt),
                                    remainDate = ticket.remainingNumber!!,  // calculator 만들기
                                    price = ticket.price,
                                    ticketStatus = TicketStatus.valueOf(ticket.state),
                                    transferFee = TransferFee.valueOf(ticket.transferFee),
                                    transMethod = TradeType.valueOf(ticket.tradeType),
                                    canNego = ticket.canNego,
                                    infoHashs = DetailHash(
                                        ticket.hasShower,
                                        ticket.hasLocker,
                                        ticket.hasClothes,
                                        ticket.hasGx,
                                        ticket.canResell,
                                        ticket.canRefund,
                                        ticket.isHolding
                                    ).mapToHashList(),
                                    description = ticket.description,
                                    tags = initTagByString(ticket.tags),//enum으로 오는 index
                                    imgList = ticket.images as List<TicketInfo.Image>,
                                    //TODO url api response 로 변경
                                    //imgList = emptyList(),
                                    isHolding = ticket.isHolding,
                                    isMembership = ticket.isMembership,
                                    remainingNumber = "10",//ticket.remainingNumber.toString(),
                                    isLikeTicket = ticket.isBookmarked
                                ),
                                onAddChatBtnClick = ::onClickChat,
                                onAddLikeClick = ::onClickLike
                            )
                            _ticketState.postValue(state)
                            _netWorkState.update { TicketDetailNetWork.Success }
                        }
                    }
                }

            }.onFailure { error ->
                Timber.e(error.message)
                _netWorkState.update { TicketDetailNetWork.Failure(error.message.toString()) }
            }
        }
    }

    //썸네일이 있는 경우 & 없는 경우 thumbnail 처리
    private fun initEmptyIcon(type: TicketKind): Uri {
        return when (type) {
            TicketKind.HEALTH -> uriConverter(
                context,
                com.depromeet.bds.R.drawable.ic_empty_health_86
            )
            TicketKind.PILATES_YOGA -> uriConverter(
                context,
                com.depromeet.bds.R.drawable.ic_empty_pilates_86
            )
            TicketKind.PT -> uriConverter(context , com.depromeet.bds.R.drawable.ic_empty_pt_86)
            else -> uriConverter(context, com.depromeet.bds.R.drawable.ic_empty_etc_86)
        }
    }

    private fun initTagByString(tag: List<String>): List<BatonHashTag> {
        return tag.map { BatonHashTag(it) }
    }


    private fun initTagByIndex(indexs: List<Int>): List<BatonHashTag> {
        val tags = ArrayList<BatonHashTag>()
        val hashs = HashTag.values()
        hashs.forEachIndexed { index, content ->
            if (indexs.contains(index)) tags.add(BatonHashTag(content.value))
        }
        return tags
    }


    fun ticketStatusHandler(status: TicketStatus) {
        val temp = _ticketState.value!!
        _ticketState.postValue(temp.copy(ticket = temp.ticket.copy(ticketStatus = status)))
    }

    private fun updateTicket(ticket: DetailTicketInfo) {
        val temp = _ticketState.value!!
        _ticketState.postValue(temp.copy(ticket = ticket))
    }

    fun deleteTicket() {
        //Api
        viewModelScope.launch {
            val ticketId = savedStateHandle.get<Int>("ticketId")!!
            ticketInfoRepository.deleteTicket(ticketId = ticketId)
        }
        addViewEvent(DetailViewEvent.EventClickDelete)
    }

    fun reportTicket(option: Int) {
        //TODO 신고 API 호출
        val ticketId = savedStateHandle.get<Int>("ticketId")!!
        viewModelScope.launch {
            //TODO 통과용 임시 api 호출
            ticketInfoRepository.getTicketInfo(
                ticketId = ticketId,
                spfManager.getMyLongitude(),
                spfManager.getMyLatitude()
            )
        }
        addViewEvent(DetailViewEvent.EventClickDelete)

    }

    private fun onClickChat() {
        //TODO 문의한 회원권인지 아닌지 판단
        addViewEvent(DetailViewEvent.EventClickChat)
    }

    private fun onClickLike() {
        //bookmark API 호출
        val temp = _ticketState.value!!
        _ticketState.postValue(temp.copy(ticket = temp.ticket.copy(isLikeTicket = !temp.ticket.isLikeTicket)))
        addViewEvent(DetailViewEvent.EventClickLike)
    }

    private fun addViewEvent(viewEvent: DetailViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: DetailViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    data class DetailTicketInfoUiState(
        val ticket: DetailTicketInfo,
        val onAddChatBtnClick: () -> Unit,
        val onAddLikeClick: () -> Unit
    ) {

        val isChatEnabled = true //TODO 문의했던 회원권인지 판단
        val chatBtnText = if (isChatEnabled) "문의하기" else "이미 문의 회원권이에요"

        val priceStr = priceFormat(ticket.price.toFloat())
        val monthTagisVisible = if (ticket.remainDate > 30) View.VISIBLE else View.GONE
        val monthPrice = priceFormat(ticket.price / 30f) + "원"
        val dayPrice = priceFormat(ticket.price / ticket.remainDate.toFloat()) + "원"

        val sellViewisVisible =ticket.ticketStatus == TicketStatus.SALE && ticket.imgList.isEmpty()
           // if (ticket.ticketStatus == TicketStatus.SALE && ticket.imgList.isEmpty()) View.VISIBLE else View.GONE
        val soldoutViewisVisible = ticket.ticketStatus == TicketStatus.SOLDOUT
            //if (ticket.ticketStatus == TicketStatus.SOLDOUT &&ticket.price ) View.VISIBLE else View.GONE
        val reservedViewisVisible =ticket.ticketStatus == TicketStatus.RESERVATION
            //if (ticket.ticketStatus == TicketStatus.RESERVATION) View.VISIBLE else View.GONE

        val canNegoStr = if (ticket.canNego) "가격제안 가능" else "가격제안 불가능"

        val startImgIndex = if (ticket.imgList.isEmpty()) "0" else "1"
        val totalImgIndex = ticket.imgList.size.toString()

        val infoTagVisible = if (ticket.infoHashs.isEmpty()) View.GONE else View.VISIBLE
        val additionalTagVisible = if (ticket.tags.isEmpty()) View.GONE else View.VISIBLE

        val emptyInfoTagVisible = if (ticket.infoHashs.isNotEmpty()) View.GONE else View.VISIBLE
        val emptyAdditionalTagVisible = if (ticket.tags.isNotEmpty()) View.GONE else View.VISIBLE

        val remainText = if (ticket.isMembership) "남은 기간" else "남은 횟수"
        val remainCount =
            if (ticket.isMembership) "${ticket.remainDate}일" else "${ticket.remainingNumber}회"

    }

}


sealed class TicketDetailNetWork() {
    object Success : TicketDetailNetWork()
    data class Failure(val msg: String) : TicketDetailNetWork()
    object Loading : TicketDetailNetWork()
}

sealed class DetailViewEvent {
    object EventClickLike : DetailViewEvent()
    object EventClickChat : DetailViewEvent()
    object EventClickDelete : DetailViewEvent()
}


