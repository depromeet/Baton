package com.depromeet.baton.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val clickListener: (FilteredTicket) -> Unit,
) : ListAdapter<FilteredTicket, TicketItemRvAdapter.TicketItemViewHolder>(TicketItemComparator()) {

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemTicketBinding.inflate(inflater, parent, false)

        binding.ibtnItemTicket.clipToOutline = true

        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        return holder.bind(currentList[position], scrollType, clickListener)
    }

    class TicketItemViewHolder(private val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: FilteredTicket,
            scrollType: String,
            clickListener: (FilteredTicket) -> Unit
        ) {
            with(binding) {
                ticket = item

                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = SCROLL_VIEW_WIDTH.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                if ((item.tags?.size ?: 0) > 2) {
                    val etcSize = (item.tags?.size ?: 0) - 2
                    itemTicketTagEtc.text = "+$etcSize"
                }

                tvItemTicketDistance.text = distanceFormatUtil(item.distance!!.toDouble())

                //TODO 좋아요 버튼
                ctvItemTicketLike.visibility = View.INVISIBLE

                setEmptyImage(item.type ?: ETC, ivItemEmpty)

                root.setOnClickListener { clickListener(item) }
            }
        }

        private fun setEmptyImage(type: String, view: ImageView) {
            when (type) {
                HEALTH -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_health_86)
                ETC -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_etc_86)
                PT -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pt_86)
                PILATES -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pilates_86)
            }
        }
    }

    companion object {
        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
        const val HEALTH = "헬스"
        const val ETC = "기타"
        const val PT = "PT"
        const val PILATES = "필라테스"
        const val SCROLL_VIEW_WIDTH = 156
    }
}

private class TicketItemComparator : DiffUtil.ItemCallback<FilteredTicket>() {
    override fun areItemsTheSame(
        oldItem: FilteredTicket,
        newItem: FilteredTicket
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FilteredTicket,
        newItem: FilteredTicket
    ): Boolean {
        return oldItem == newItem
    }
}