package com.depromeet.baton.presentation.ui.detail.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.depromeet.baton.R
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.presentation.ui.detail.model.*
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    application : Application,
    private val spfManager: BatonSpfManager,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {


    private val context : Context = application
    //추후 변경
    private val _ticketState= MutableLiveData<DetailTicketInfoUiState>()
    val ticketState get() =  _ticketState

    private val _viewEvents: MutableStateFlow<List<DetailViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _netWorkState = MutableStateFlow<TicketDetailNetWork>(TicketDetailNetWork.Loading)
    val netWorkState = _netWorkState.asStateFlow()

    init {
        viewModelScope.launch {
            //API 호출
            runCatching {
                val ticketId = savedStateHandle.get<Int>("ticketId")
                val tempUserID = 3 // 임시 userID
                val tempSellerID = 3 // 임시 userID

                val ticket =  DetailTicketInfoUiState(
                    DetailTicketInfo(
                        ticketId= 1,
                        ticketType =TicketKind.PILATES_YOGA,
                        seller = DetailTicketInfo.Seller(tempSellerID, "batong", true),
                        isOwner = tempUserID ==tempSellerID,
                        detailUrl = "https://map.naver.com/v5/search/${"휴메이크휘트니스석촌점"}",
                        emptyIcon = initEmptyIcon(TicketKind.PILATES_YOGA),
                        location = DetailLocationInfo(location = "바통 휘트니스", "도로명 주소",127.0297999,37.4920448,1000F),
                        createdDate = "2022.03.03",
                        remainDate = 60,  // calculator 만들기
                        price = 100000,
                        ticketState = TicketStatus.SOLDOUT,
                        transferFee = TransferFee.NONE,
                        transMethod = TransactionMethod.FACE,
                        canNego = false,
                        infoHashs = DetailHash(true,true,false,true,true,false).mapToHashList(),
                        description = "헬스 회원권  60일권  200,000원으로 저렴하게 양도합니다!\u2028- 접근성 좋음 주변에 버스터미널, 정류장 등 가까움\n" +
                                "- 선생님도 친절/대회 수상이력 당연O, 몸 컨디션 체크 등등 설명도 정말 잘해주세요!!\n" +
                                "- PT시간은 4시이후, 저녁시간대 가능",
                        tags = initTagByIndex(arrayListOf(1,4,5)),//enum으로 오는 index
                        /*imgList = arrayListOf(TicketImage(1, uriConverter(context, R.drawable.dummy1).toString(),uriConverter(context, R.drawable.dummy1).toString(),true)
                        ,TicketImage(1, uriConverter(context, R.drawable.dummy2).toString(),uriConverter(context, R.drawable.dummy2).toString(),false)),*/
                        imgList = emptyList(),
                        isHolding = true,
                        isMembership = true,
                        remainingNumber = "60",
                        isLikeTicket = true
                    ),
                    onAddChatBtnClick = :: onClickChat,
                    onAddMenuClick = ::onClickChat,
                    onAddLikeClick = :: onClickLike
                )

                return@runCatching ticket
            }.onSuccess {
                data -> run{
                    _netWorkState.update { TicketDetailNetWork.Success }
                    //TODO data Entity 처리
                    _ticketState.postValue(data)
                 }
            }.onFailure {
                error ->
                _netWorkState.update { TicketDetailNetWork.Failure(error.message.toString()) }
            }
        }

    }

    //썸네일이 있는 경우 & 없는 경우 thumbnail 처리
    private fun initEmptyIcon( type : TicketKind) : Uri{
        return when(type){
            TicketKind.GYM -> uriConverter(context , com.depromeet.bds.R.drawable.ic_empty_health_86)
            TicketKind.PILATES_YOGA -> uriConverter(context, com.depromeet.bds.R.drawable.ic_empty_pilates_86)
            else -> uriConverter(context, com.depromeet.bds.R.drawable.ic_empty_etc_86)
        }
    }


    fun initTagByIndex(indexs : List<Int>) : List<BatonHashTag>{
        val tags = ArrayList<BatonHashTag>()
        val hashs = HashTag.values()
        hashs.forEachIndexed{
            index, content ->  if(indexs.contains(index)) tags.add(BatonHashTag(content.value))
        }
        return tags
    }


    fun ticketStatusHandler(status: TicketStatus){
        val temp= _ticketState.value!!
        _ticketState.postValue(temp.copy(ticket= temp.ticket.copy(ticketState = status)))
    }

    private fun updateTicket(ticket : DetailTicketInfo){
        val temp = _ticketState.value!!
        _ticketState.postValue(temp.copy( ticket= ticket))
    }

    fun deleteTicket(ticketId : Int){
        //Api
    }

    fun reportTicket(option : Int){
        //신고 API 호출
    }

    private fun onClickChat(){
        //TODO 문의한 회원권인지 아닌지 판단
        addViewEvent(DetailViewEvent.EventClickChat)
    }

    private fun onClickLike(){
        //bookmark API 호출
        val temp = _ticketState.value!!
        _ticketState.postValue(temp.copy(ticket = temp.ticket.copy(isLikeTicket = !temp.ticket.isLikeTicket) ))
        addViewEvent(DetailViewEvent.EventClickLike)
    }

    private fun addViewEvent(viewEvent: DetailViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: DetailViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    data class DetailTicketInfoUiState(
        val ticket : DetailTicketInfo,
        val onAddChatBtnClick: ()-> Unit,
        val onAddMenuClick : () -> Unit,
        val onAddLikeClick :()->Unit
    ){

        val isChatEnabled = true //TODO 문의했던 회원권인지 판단
        val chatBtnText = if(isChatEnabled)"문의하기" else "이미 문의 회원권이에요"

        val priceStr = priceFormat(ticket.price.toFloat())
        val monthTagisVisible = if(ticket.remainDate > 30) View.VISIBLE else View.GONE
        val monthPrice = priceFormat(ticket.price/30f) +"원"
        val dayPrice = priceFormat(ticket.price/ticket.remainDate.toFloat()) +"원"

        val sellViewisVisible  = if( ticket.ticketState ==TicketStatus.SALE && ticket.imgList.isEmpty()) View.VISIBLE else View.GONE
        val soldoutViewisVisible  = if(ticket.ticketState ==TicketStatus.SOLDOUT ) View.VISIBLE else View.GONE
        val reservedViewisVisible = if( ticket.ticketState==TicketStatus.RESERVATION)  View.VISIBLE else View.GONE

        val canNegoStr = if(ticket.canNego) "가격제안 가능" else "가격제안 불가능"

        val startImgIndex = if(ticket.imgList.isEmpty())"0" else "1"
        val totalImgIndex = ticket.imgList.size.toString()

    }

}


sealed class TicketDetailNetWork(){
    object Success:  TicketDetailNetWork()
    data class Failure(val msg:String) :  TicketDetailNetWork()
    object Loading : TicketDetailNetWork()
}

sealed class DetailViewEvent{
    object EventClickLike : DetailViewEvent()
    object EventClickChat : DetailViewEvent()
    object EventClickCopy : DetailViewEvent()
    object EventClickWebView :DetailViewEvent()
    object EventClickMap : DetailViewEvent()
    object EventClickMoreItem : DetailViewEvent()
}


