package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val context: Context,
    private val clickListener: (ResponseFilteredTicket) -> Unit
) : ListAdapter<ResponseFilteredTicket, TicketItemRvAdapter.TicketItemViewHolder>(diffCallback) {

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
        fun bind(item: ResponseFilteredTicket, position: Int) {
            with(binding) {
                //가로스크롤뷰
                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = 160.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                //tvItemTicketCardBadge.text = item.card
                tvItemTicketShopName.text = item.location
                tvItemTicketPrice.text = item.price.toString()
                tvItemTicketRemainingDay.text = item.expiryDate //todo 남은일자 계산해야댐 ㅠ
                tvItemTicketPlace.text = item.address
                tvItemTicketDistance.text = distanceFormatUtil(item.distance)//item.distance.toString()

                Glide.with(context)
                    .load(item.mainImage)
                    .transform(CenterCrop(), RoundedCorners(4.toPx()))
                    .into(binding.ibtnItemTicket)

                setLikeBtnClickListener(ctvItemTicketLike)

                if (item.mainImage==null) setEmptyImage(position, ibtnItemTicket)

                root.setOnClickListener {
                    clickListener(item)
                }
            }
        }
    }

    private fun setEmptyImage(position: Int, view: AppCompatImageButton) {
        when (position % 4) {
            0 -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_health_86)
            1 -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_etc_86)
            2 -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pt_86)
            3 -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pilates_86)
        }
    }

    private fun setLikeBtnClickListener(view: CheckedTextView) {
        view.setOnClickListener {
            view.toggle()
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseFilteredTicket>() {
            override fun areItemsTheSame(oldItem: ResponseFilteredTicket, newItem: ResponseFilteredTicket): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ResponseFilteredTicket, newItem: ResponseFilteredTicket): Boolean =
                oldItem.id == newItem.id
        }

        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
    }
}
