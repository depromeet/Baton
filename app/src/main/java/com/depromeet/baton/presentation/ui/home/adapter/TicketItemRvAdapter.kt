package com.depromeet.baton.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.util.SimpleDiffUtil
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val clickListener: (ResponseFilteredTicket) -> Unit
) : ListAdapter<ResponseFilteredTicket, TicketItemRvAdapter.TicketItemViewHolder>(SimpleDiffUtil()) {

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
             //   ticket = item
                executePendingBindings()
                //tvItemTicketCardBadge.text = item.card todo 서버에서 추가 후 작업

      /*          if ((item.tags?.size ?: 0) > 2) {
                    val etcSize = (item.tags?.size ?: 0) - 2
                    itemTicketTagEtc.text = "+$etcSize"
                }*/

                //가로스크롤뷰
                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = 160.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                setLikeBtnClickListener(ctvItemTicketLike) //좋아요 버튼

       //         if (item.mainImage?.isEmpty() == true) setEmptyImage(position, ibtnItemTicket) //엠티 뷰

                root.setOnClickListener { clickListener(item) } //상세페이지로
            }
        }
    }

    private fun setEmptyImage(position: Int, view: AppCompatImageView) {
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
        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
    }
}
