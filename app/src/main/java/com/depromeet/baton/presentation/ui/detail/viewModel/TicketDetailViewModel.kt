package com.depromeet.baton.presentation.ui.detail.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.BookmarkRepository
import com.depromeet.baton.domain.repository.TicketInfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.ui.detail.model.*
import com.depromeet.baton.presentation.util.dateFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val bookmarkRepository: BookmarkRepository,
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
                            val userId = authRepository.authInfo!!.userId
                            val sellerId = ticket.seller.id
                            val state = DetailTicketInfoUiState(
                                DetailTicketInfo(
                                    ticketId = ticket.id,
                                    ticketType = TicketKind.valueOf(ticket.type),
                                    seller = DetailTicketInfo.Seller(
                                        sellerId,
                                        ticket.seller.nickname,
                                        ticket.seller.createdOn
                                    ),
                                    isOwner = userId == sellerId,
                                    detailUrl = "https://map.naver.com/v5/search/${ticket.location.replace(" ","")}",
                                    mapUrl = "https://map.naver.com/index.nhn?slng=${spfManager.getMyLongitude()}&slat=${spfManager.getMyLatitude()}" +
                                            "&stext=내 위치&elng=${ticket!!.longitude}&elat=${ticket!!.latitude}" +
                                            "&pathType=3&showMap=true&etext=${ticket!!.location}&menu=route",
                                    emptyIcon = initEmptyIcon(ticket.type),
                                    location = DetailLocationInfo(
                                        location = ticket.location,
                                        ticket.address,
                                        ticket.longitude,
                                        ticket.latitude,
                                        ticket.distance.toFloat()
                                    ),
                                    createdDate = dateFormatUtil(ticket.createAt),
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
                                    isHolding = ticket.isHolding,
                                    isMembership = ticket.isMembership,
                                    remainDate = ticket.remainingDay,
                                    remainingNumber = ticket.remainingNumber,
                                    bookmarkId = ticket.bookmarkId,
                                    isLikeTicket = ticket.bookmarkId!=null,
                                    bookmarkView = ticket.bookmarkCount ?:0,
                                    countView = ticket.viewCount
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
    private fun initEmptyIcon(type: String): Uri {
        return when (type) {
            TicketKind.HEALTH.name -> uriConverter(
                context,
                com.depromeet.bds.R.drawable.ic_empty_health_86
            )
            TicketKind.PILATES_YOGA.name -> uriConverter(
                context,
                com.depromeet.bds.R.drawable.ic_empty_pilates_86
            )
            TicketKind.PT.name -> uriConverter(context , com.depromeet.bds.R.drawable.ic_empty_pt_86)
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
        viewModelScope.launch {
            runCatching {
                _netWorkState.update { TicketDetailNetWork.Loading }
                ticketInfoRepository.updateTicketState(temp.ticket.ticketId, status.name)
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{
                        _netWorkState.update { TicketDetailNetWork.Success }
                        _ticketState.postValue(temp.copy(ticket = temp.ticket.copy(ticketStatus = status)))
                    }
                    is NetworkResult.Error->{
                        Timber.e("${it.message}")
                        _netWorkState.update { TicketDetailNetWork.Failure("판매상태 변경에 실패했습니다.") }
                    }
                }
            }
        }
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
    }

    fun reportSeller(option: Int){
        val ticketId = savedStateHandle.get<Int>("ticketId")!!
        viewModelScope.launch {
            //TODO 통과용 임시 api 호출
            ticketInfoRepository.getTicketInfo(
                ticketId = ticketId,
                spfManager.getMyLongitude(),
                spfManager.getMyLatitude()
            )
        }
    }

    private fun onClickChat() {
        //TODO 문의한 회원권인지 아닌지 판단
        addViewEvent(DetailViewEvent.EventClickChat)
    }

    private fun onClickLike() {
        //bookmark API 호출
        val temp = ticketState.value!!
        ticketState.value?.let {
            if(it.ticket.isLikeTicket){
                _ticketState.postValue(temp.copy(ticket = temp.ticket.copy( isLikeTicket = false,  bookmarkView= temp.ticket.bookmarkView!! -1 )))
                addViewEvent(DetailViewEvent.EventClickUnLike)
            }else{
                _ticketState.postValue(temp.copy(ticket = temp.ticket.copy(isLikeTicket = true, bookmarkView= temp.ticket.bookmarkView!! +1,)))
                addViewEvent(DetailViewEvent.EventClickLike)
            }
        }
    }

    fun checkLikeStatus(){
        if(ticketState.value!!.ticket.bookmarkId == null && ticketState.value!!.ticket.isLikeTicket) addBookmark()
        else if(ticketState.value!!.ticket.bookmarkId != null && !ticketState.value!!.ticket.isLikeTicket) deleteBookmark()
    }

    private fun addBookmark(){
        val temp = ticketState.value!!
        viewModelScope.launch {
            runCatching {
                val userId = authRepository.authInfo!!.userId
                bookmarkRepository.postBookmark(userId,temp.ticket.ticketId)
            }.onSuccess { res->
                when(res){
                    is NetworkResult.Success ->{_ticketState.postValue(temp.copy(ticket = temp.ticket.copy( bookmarkId = res.data!!.id)))}
                    is NetworkResult.Error->{
                        Timber.e(res.message)
                    }
                }
            }.onFailure {  Timber.e(it.message) }
        }
    }

    private fun deleteBookmark(){
        val temp = _ticketState.value!!
       viewModelScope.launch {
            runCatching {
                bookmarkRepository.deleteBookmark(temp.ticket.bookmarkId!!)
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{}
                    is NetworkResult.Error->{
                        Timber.e(it.message)
                    }
                }
            }.onFailure {  Timber.e(it.message) }
        }
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
        val chatBtnText = if(ticket.isOwner)"채팅목록" else if (isChatEnabled) "채팅하기" else "이미 문의 회원권이에요"

        val priceStr = priceFormat(ticket.price.toFloat())+"원"
        val monthTagisVisible = if (ticket.remainDate!=null&& ticket.isMembership && ticket.remainDate > 30) View.VISIBLE else View.GONE
        val monthPrice = priceFormat(ticket.price / 30f) + "원"
        val dayTagisVisible = if (ticket.isMembership) View.VISIBLE else View.GONE
        val dayPrice = if(ticket.remainDate!=null) priceFormat(ticket.price / ticket.remainDate!!.toFloat()) + "원" else ""

        val sellViewisVisible = ticket.ticketStatus == TicketStatus.SALE && ticket.imgList.isEmpty()
        val soldoutViewisVisible = ticket.ticketStatus == TicketStatus.DONE
        val reservedViewisVisible = ticket.ticketStatus == TicketStatus.RESERVED

        val canNegoStr = if (ticket.canNego) "가격제안 가능" else "가격제안 불가능"

        val startImgIndex = if (ticket.imgList.isEmpty()) "0" else "1"
        val totalImgIndex = ticket.imgList.size.toString()

        val infoTagVisible = if (ticket.infoHashs.isEmpty()) View.GONE else View.VISIBLE
        val additionalTagVisible = if (ticket.tags.isEmpty()) View.GONE else View.VISIBLE

        val emptyInfoTagVisible = if (ticket.infoHashs.isNotEmpty()) View.GONE else View.VISIBLE
        val emptyAdditionalTagVisible = if (ticket.tags.isNotEmpty()) View.GONE else View.VISIBLE

        val remainText = if (ticket.isMembership) "남은 기간" else "남은 횟수"
        val remainCount = if (ticket.isMembership) "${ticket.remainDate}일" else "${ticket.remainingNumber}회"

        //bookmark id 가 null 이면 관심 상품 아님
        val bookmarkState  = ticket.isLikeTicket

    }

}


sealed class TicketDetailNetWork() {
    object Success : TicketDetailNetWork()
    data class Failure(val msg: String) : TicketDetailNetWork()
    object Loading : TicketDetailNetWork()
}

sealed class DetailViewEvent {
    object EventClickLike : DetailViewEvent()
    object EventClickUnLike : DetailViewEvent()
    object EventClickChat : DetailViewEvent()
    object EventClickDelete : DetailViewEvent()
}


