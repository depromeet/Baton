package com.depromeet.baton.presentation.ui.mypage.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.databinding.ItemTicketPurchaseBinding
import com.depromeet.baton.databinding.ItemTicketSaleFooterBinding
import com.depromeet.baton.databinding.ItemTicketSaleHeaderBinding
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.util.dateFormatUtil
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.util.SimpleDiffUtil
import com.depromeet.bds.utils.toPx

class PurchaseTicketItemAdapter(
    private val context: Context,
    private val onClickItem: (SaleTicketListItem) -> Unit,
) : ListAdapter<SaleTicketListItem, PurchaseTicketItemAdapter.SaleTicketViewHolder>(SimpleDiffUtil()) {


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
            SaleTicketListItem.PurchasedItem.VIEW_TYPE->{
                val binding = DataBindingUtil.inflate<ItemTicketPurchaseBinding>(layoutInflater, viewType, parent, false)
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

    inner class SaleTicketItemViewHolder(private val binding: ItemTicketPurchaseBinding) :
        SaleTicketViewHolder(binding) {
        override fun bind(item: SaleTicketListItem, position: Int) {
            with(binding) {
                itemSaleNameTv.text = item.ticket.data.location
                itemSalePriceTv.text = item.ticket.data.price.toString()
                itemSaleRemainDateTv.text = item.ticket.data.remainingNumber.toString()
                itemSaleLocationTv.text = item.ticket.data.address
                itemSaleDistanceTv.text = distanceFormatUtil(item.ticket.data.distance)
               // itemSaleBadgeTv.text=  item.ticket.card

                Glide.with(context)
                    .load(item.ticket.data.mainImage)
                    .transform(CenterCrop(), RoundedCorners(4.toPx()))
                    .into(binding.itemSaleImageIv)

                root.setOnClickListener {
                    onClickItem(item)
                }
            }
        }
    }




  /*  companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SaleTicketListItem>() {
            override fun areItemsTheSame(oldItem: SaleTicketListItem, newItem: SaleTicketListItem): Boolean =
                oldItem.ticket.shopName == newItem.ticket.shopName

            override fun areContentsTheSame(oldItem: SaleTicketListItem, newItem: SaleTicketListItem): Boolean =
                oldItem.ticket.shopName == newItem.ticket.shopName
        }

    }*/
}
