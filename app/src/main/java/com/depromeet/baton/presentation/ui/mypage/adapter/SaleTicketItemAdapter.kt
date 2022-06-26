package com.depromeet.baton.presentation.ui.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.databinding.ItemTicketSaleBinding
import com.depromeet.baton.databinding.ItemTicketSaleFooterBinding
import com.depromeet.baton.databinding.ItemTicketSaleHeaderBinding
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.domain.model.TicketStatus
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.util.dateFormatUtil
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.bds.utils.toPx
import timber.log.Timber

class SaleTicketItemAdapter(
    private val context: Context,
    private val onClickMenu: (SaleTicketListItem, View) -> Unit,
    private val onClickStatusMenu :(SaleTicketListItem,Int)->Unit
) : ListAdapter<SaleTicketListItem, SaleTicketItemAdapter.SaleTicketViewHolder>(diffCallback ) {

    private var selectedItem :SaleTicketListItem?=null
    private var selectedPos : Int ?=null

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaleTicketViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        return  when(viewType){
            SaleTicketListItem.Header.VIEW_TYPE->{
                val binding = DataBindingUtil.inflate<ItemTicketSaleHeaderBinding>(layoutInflater, viewType, parent, false)
                SaleTicketHeaderViewHolder(binding)
            }
            SaleTicketListItem.Footer.VIEW_TYPE->{
                 val binding = DataBindingUtil.inflate<ItemTicketSaleFooterBinding>(layoutInflater, viewType, parent, false)
                 SaleTicketFooterViewHolder(binding)
            }
            SaleTicketListItem.Item.VIEW_TYPE->{
                val binding = DataBindingUtil.inflate<ItemTicketSaleBinding>(layoutInflater, viewType, parent, false)
                 SaleTicketItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Cannot create ViewHolder for view type: $viewType")
        }
    }

    abstract class SaleTicketViewHolder(
        itemView: ViewDataBinding
    ) : RecyclerView.ViewHolder(itemView.root) {
        abstract fun bind(item: SaleTicketListItem, position: Int)
    }

    override fun onBindViewHolder(holder: SaleTicketViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class SaleTicketHeaderViewHolder(private val binding : ItemTicketSaleHeaderBinding) : SaleTicketViewHolder(binding){
        override fun bind(item: SaleTicketListItem , position : Int) {
            binding.itemTicketSaleHeaderDateTv.text = dateFormatUtil(item.ticket.data.createAt)
        }
    }

    inner class SaleTicketFooterViewHolder(private val binding : ItemTicketSaleFooterBinding) : SaleTicketViewHolder(binding){
        override fun bind(item: SaleTicketListItem, position: Int) {
        }
    }

    inner class SaleTicketItemViewHolder(private val binding: ItemTicketSaleBinding) :
        SaleTicketViewHolder(binding) {
        override fun bind(item: SaleTicketListItem, position: Int) {
            with(binding) {
                itemSaleNameTv.text =  item.ticket.data.location
                itemSalePriceTv.text = priceFormat(item.ticket.data.price.toFloat())+"원"

                itemSaleRemainDateTv.text =
                    if(item.ticket.data.remainingNumber!=null) "${item.ticket.data.remainingNumber}회 남음"
                    else "${item.ticket.data.remainingDay}일 남음"

                itemSaleLocationTv.text = if(item.ticket.data.address.length > 15) item.ticket.data.address.substring(0,15)+"..." else item.ticket.data.address
                itemSaleDistanceTv.text = distanceFormatUtil( item.ticket.data.distance)

                itemSaleBadgeTv.text=  when(TicketKind.valueOf(item.ticket.data.type).ordinal){
                    0 -> "헬스"
                    1-> "PT"
                    2-> "필라테스"
                    else-> "ETC"
                }
                when(item.ticket.data.state){
                     TicketStatus.SALE.name ->{
                         itemSaleStatusView.visibility = View.GONE
                         itemSaleStatusChip.visibility =View.GONE
                         itemSaleGap.visibility = View.GONE
                     }
                    TicketStatus.RESERVED.name->{
                        val resource= setUi(TicketStatus.RESERVED)
                        itemSaleStatusIc.setImageResource(resource.icon)
                        itemSaleStatusTv.text = resource.title
                        itemSaleStatusChip.text = resource.title
                        itemSaleStatusView.visibility = View.VISIBLE
                    }
                    TicketStatus.DONE.name->{
                        val resource= setUi(TicketStatus.DONE)
                        itemSaleStatusIc.setImageResource(resource.icon)
                        itemSaleStatusTv.text = resource.title
                        itemSaleStatusChip.text = resource.title
                        itemSaleMenuBtn.visibility=View.GONE
                        itemSaleStatusView.visibility = View.VISIBLE
                    }
                }

                if(item.ticket.data.mainImage !=null)
                    Glide.with(context)
                    .load(item.ticket.data.mainImage)
                    .transform(CenterCrop(), RoundedCorners(4.toPx()))
                    .into(binding.itemSaleImageIv)
                else{
                    setEmptyImage(item.ticket.data.type, itemSaleImageIv)
                }

                itemSaleMenuBtn.setOnClickListener {
                    selectedItem=item
                    selectedPos= position
                    onClickMenu(item, binding.itemSaleMenuBtn)
                }

                itemSaleChangeBtn.setOnClickListener {
                    selectedItem=item
                    selectedPos= position
                    onClickStatusMenu(item, selectedPos!!)
                }

                itemSaleImageIv.setOnClickListener {
                    TicketDetailActivity.start(context,item.ticket.data.id)
                }
            }
        }
    }


    fun getSelectedItem(): Pair<SaleTicketListItem?,Int?> = Pair(selectedItem,selectedPos)

    fun removeSelectedItem(position: Int){
        val item = getItem(position)
        val newList = currentList.toMutableList()
        if (position < currentList.size){
            val createdAt = dateFormatUtil(newList.get(position).ticket.date)
            val hasChild= newList.find{ item.ticket.data.id !=it.ticket.data.id && it is SaleTicketListItem.Item && createdAt == dateFormatUtil(it.ticket.date)}
            if(hasChild ==null){
                if(newList.size<3){
                    //footer가 없는경우
                    newList.removeAt(position) //item 삭제
                    newList.removeAt(position-1) //header삭제

                }else if(position ==currentList.lastIndex){
                    newList.removeAt(position) //item 삭제
                    newList.removeAt(position-1) //header삭제
                    newList.removeAt(position-2) //footer 삭제
                }
                else{
                    newList.removeAt(position+1) //footer 삭제
                    newList.removeAt(position) //item 삭제
                    newList.removeAt(position-1) //header삭제
                }
            }else{
                newList.removeAt(position)
            }
        }
        submitList(newList)
    }


    fun setUi(state: TicketStatus):TicketStateUi{
        when(state){
            TicketStatus.SALE -> return TicketStateUi.SaleUi
            TicketStatus.RESERVED ->return TicketStateUi.ReservationUi
            TicketStatus.DONE ->return  TicketStateUi.SoldoutUi
        }
    }

    private fun setEmptyImage(type : String ,view:ImageView) {
        when (type) {
            TicketKind.HEALTH.name -> view.setImageResource(com.depromeet.bds.R.drawable.ic_img_empty_health_44)
            TicketKind.PT.name-> view.setImageResource(com.depromeet.bds.R.drawable.ic_img_empty_pt_44)
            TicketKind.PILATES_YOGA.name-> view.setImageResource(com.depromeet.bds.R.drawable.ic_img_empty_pilates_44)
            TicketKind.ETC.name -> view.setImageResource(com.depromeet.bds.R.drawable.ic_img_empty_etc_44)
        }
    }

    enum class TicketStateUi(val icon: Int,val title: String){
        SaleUi(0,""),
        ReservationUi(com.depromeet.bds.R.drawable.ic_time_line_20, "예약중"),
        SoldoutUi(com.depromeet.bds.R.drawable.ic_check_circle_line_20,"거래완료")
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SaleTicketListItem>() {
            override fun areItemsTheSame(oldItem: SaleTicketListItem, newItem: SaleTicketListItem): Boolean =
                false
            override fun areContentsTheSame(oldItem: SaleTicketListItem, newItem: SaleTicketListItem): Boolean =
                false
        }

    }
}
