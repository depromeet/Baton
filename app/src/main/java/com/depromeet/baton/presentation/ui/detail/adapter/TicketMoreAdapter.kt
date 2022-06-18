package com.depromeet.baton.presentation.ui.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.bds.utils.toPx


class TicketMoreAdapter(
    private val context: Context,
    private val clickListener: (TicketSimpleInfo) -> Unit
) : ListAdapter<TicketSimpleInfo, TicketMoreAdapter.TicketItemViewHolder>(diffCallback) {

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
        fun bind(item: TicketSimpleInfo, position: Int) {
            with(binding) {
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.width = 160.toPx()
                ctlItemTicketContainer.layoutParams = lp

                tvItemTicketShopName.text = item.location
                tvItemTicketPrice.text = priceFormat(item.price.toFloat())
                tvItemTicketRemainingDay.text =
                    if(item.isMembership) item.remainingDay.toString()+"일"
                    else item.remainingNumber.toString()+"회"

                tvItemTicketPlace.text = item.address
                tvItemTicketDistance.text = distanceFormatUtil(item.distance)
                ctvItemTicketLike.isChecked = item.bookmarkId!=null

                if (item.mainImage!= null)
                    Glide.with(context)
                        .load(item.mainImage)
                        .transform(CenterCrop(), RoundedCorners(4.toPx()))
                        .into(binding.ibtnItemTicket)

                setLikeBtnClickListener(ctvItemTicketLike)

                if (item.mainImage== null &&item.type!=null) setEmptyImage(TicketKind.valueOf(item.type),ibtnItemTicket)

                root.setOnClickListener {
                    clickListener(item)
                }
            }
        }
    }

    private fun setEmptyImage(state : TicketKind, view:ImageView) {
        when (state) {
            TicketKind.HEALTH -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_health_86)
            TicketKind.ETC -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_etc_86)
            TicketKind.PT -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pt_86)
            TicketKind.PILATES_YOGA -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pilates_86)
        }
    }

    private fun setLikeBtnClickListener(view: CheckedTextView) {
        view.setOnClickListener {
            view.toggle()
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<TicketSimpleInfo>() {
            override fun areItemsTheSame(oldItem: TicketSimpleInfo, newItem:TicketSimpleInfo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TicketSimpleInfo, newItem: TicketSimpleInfo,): Boolean =
                oldItem.id == newItem.id
        }
    }
}
