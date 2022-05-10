package com.depromeet.baton.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemTicketHorizontalBinding
import com.depromeet.baton.databinding.ItemTicketVerticalBinding
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.bds.R

class TicketItemRvAdapter<B : ViewDataBinding>(
    private val layout: Int
) : ListAdapter<TicketItem, TicketItemRvAdapter<B>.TicketItemViewHolder<B>>(CardMeComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketItemViewHolder<B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(layoutInflater, layout, parent, false)
        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder<B>, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class TicketItemViewHolder<B : ViewDataBinding>(private val binding: B) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TicketItem, position: Int) {
            when (binding) {
                is ItemTicketVerticalBinding -> {
                    with(binding as ItemTicketVerticalBinding) {
                        tvItemTicketVerticalCardBadge.text = item.card
                        tvTicketItemVerticalShopName.text = item.shopName
                        tvTicketItemVerticalPrice.text = item.price
                        tvTicketItemVerticalRemainingDay.text = item.remainingDay
                        tvItemTicketVerticalPlace.text = item.place
                        tvTicketItemVerticalPrice.text = item.price

                        setEmptyImage(position, ibtnItemTicketVertical)
                        setLikeBtnClickListener(ctvItemVerticalLike)
                    }
                }
                is ItemTicketHorizontalBinding -> {
                    with(binding as ItemTicketHorizontalBinding) {
                        tvItemTicketHorizontalCardBadge.text = item.card
                        tvTicketItemHorizontalShopName.text = item.shopName
                        tvTicketItemHorizontalPrice.text = item.price
                        tvTicketItemHorizontalRemainingDay.text = item.remainingDay
                        tvTicketItemHorizontalPlace.text = item.place
                        tvTicketItemHorizontalPrice.text = item.price

                        setEmptyImage(position, ibtnItemTicketHorizontal)
                        setLikeBtnClickListener(ctvTicketItemHorizontal)
                    }
                }
            }
        }
    }

    private fun setEmptyImage(position: Int, view: AppCompatImageButton) {
        when (position % 4) {
            0 -> view.setImageResource(R.drawable.ic_empty_health_86)
            1 -> view.setImageResource(R.drawable.ic_empty_etc_86)
            2 -> view.setImageResource(R.drawable.ic_empty_pt_86)
            3 -> view.setImageResource(R.drawable.ic_empty_pilates_86)
        }
    }
}

private fun setLikeBtnClickListener(view: CheckedTextView) {
    view.setOnClickListener {
        view.toggle()
    }
}

private class CardMeComparator : DiffUtil.ItemCallback<TicketItem>() {
    override fun areItemsTheSame(
        oldItem: TicketItem,
        newItem: TicketItem
    ): Boolean {
        return oldItem.shopName == newItem.shopName
    }

    override fun areContentsTheSame(
        oldItem: TicketItem,
        newItem: TicketItem
    ): Boolean {
        return oldItem.shopName == newItem.shopName
    }
}
