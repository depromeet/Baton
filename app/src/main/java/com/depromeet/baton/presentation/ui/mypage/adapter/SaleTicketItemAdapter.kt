package com.depromeet.baton.presentation.ui.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.databinding.ItemTicketSaleBinding
import com.depromeet.baton.presentation.ui.mypage.SaleTicketItem
import com.depromeet.bds.utils.toPx

class SaleTicketItemAdapter(
    private val context: Context,
    private val onClickMenu: (SaleTicketItem, View) -> Unit,
    private val onClickStatusMenu :(SaleTicketItem)->Unit
) : ListAdapter<SaleTicketItem, SaleTicketItemAdapter.SaleTicketItemViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaleTicketItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemTicketSaleBinding>(layoutInflater, com.depromeet.baton.R.layout.item_ticket_sale, parent, false)
        return SaleTicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleTicketItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class SaleTicketItemViewHolder(private val binding: ItemTicketSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SaleTicketItem, position: Int) {
            with(binding) {
                itemSaleNameTv.text = item.shopName
                itemSalePriceTv.text = item.price
                itemSaleRemainDateTv.text = item.remainingDay
                itemSaleLocationTv.text = item.place
                itemSaleDistanceTv.text = item.distance
                Glide.with(context)
                    .load(item.img)
                    .transform(CenterCrop(), RoundedCorners(4.toPx()))
                    .into(binding.itemSaleImageIv)

                itemSaleMenuBtn.setOnClickListener {
                    onClickMenu(item, binding.itemSaleMenuBtn)
                }

                itemSaleChangeBtn.setOnClickListener {
                    onClickStatusMenu(item)
                }

            }
        }
    }




    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SaleTicketItem>() {
            override fun areItemsTheSame(oldItem: SaleTicketItem, newItem: SaleTicketItem): Boolean =
                oldItem.shopName == newItem.shopName

            override fun areContentsTheSame(oldItem: SaleTicketItem, newItem: SaleTicketItem): Boolean =
                oldItem.shopName == newItem.shopName
        }

    }
}
