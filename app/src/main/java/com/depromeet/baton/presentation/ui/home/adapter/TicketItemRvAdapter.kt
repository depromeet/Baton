package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.bds.R
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val context: Context,
    private val clickListener: (TicketItem) -> Unit
) : ListAdapter<TicketItem, TicketItemRvAdapter.TicketItemViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemTicketBinding>(layoutInflater, com.depromeet.baton.R.layout.item_ticket, parent, false)
        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class TicketItemViewHolder(private val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TicketItem, position: Int) {
            with(binding) {
                //가로스크롤뷰
                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = 160.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                tvItemTicketCardBadge.text = item.card
                tvItemTicketShopName.text = item.shopName
                tvItemTicketPrice.text = item.price
                tvItemTicketRemainingDay.text = item.remainingDay
                tvItemTicketPlace.text = item.place
                tvItemTicketPrice.text = item.price
                Glide.with(context)
                    .load(item.img)
                    .transform(CenterCrop(), RoundedCorners(4.toPx()))
                    .into(binding.ibtnItemTicket)

                setLikeBtnClickListener(ctvItemTicketLike)
                root.setOnClickListener {
                    clickListener(item)
                }

                //todo 중간발표용 하트 클릭
                if(position==2 || position==5){
                    ctvItemTicketLike.isChecked=true
                }
            }
        }
    }


    private fun setEmptyImage(position: Int, view: AppCompatImageButton) {
        /*     when (position % 4) {
                 0 -> view.setImageResource(R.drawable.ic_empty_health_86)
                 1 -> view.setImageResource(R.drawable.ic_empty_etc_86)
                 2 -> view.setImageResource(R.drawable.ic_empty_pt_86)
                 3 -> view.setImageResource(R.drawable.ic_empty_pilates_86)
             }*/
    }

    private fun setLikeBtnClickListener(view: CheckedTextView) {
        view.setOnClickListener {
            view.toggle()
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<TicketItem>() {
            override fun areItemsTheSame(oldItem: TicketItem, newItem: TicketItem): Boolean =
                oldItem.shopName == newItem.shopName

            override fun areContentsTheSame(oldItem: TicketItem, newItem: TicketItem): Boolean =
                oldItem.shopName == newItem.shopName
        }

        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
    }
}
