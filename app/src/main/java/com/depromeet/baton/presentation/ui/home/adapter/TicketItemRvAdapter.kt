package com.depromeet.baton.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.util.SimpleDiffUtil
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val clickListener: (FilteredTicket) -> Unit
) : ListAdapter<FilteredTicket, TicketItemRvAdapter.TicketItemViewHolder>(SimpleDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        return holder.bind(currentList[position], position)
    }

    inner class TicketItemViewHolder(private val binding: ItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilteredTicket, position: Int) {
            with(binding) {
                ticket = item
                executePendingBindings()

                //가로스크롤뷰
                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = 156.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                //이미지 라운드 처리
                ibtnItemTicket.clipToOutline = true

                //태그
                if ((item.tags?.size ?: 0) > 2) {
                    val etcSize = (item.tags?.size ?: 0) - 2
                    itemTicketTagEtc.text = "+$etcSize"
                }

                //좋아요 버튼
                setLikeBtnClickListener(ctvItemTicketLike)

                //엠티뷰
                setEmptyImage(position, ivItemEmpty)

                //상세페이지로
                root.setOnClickListener { clickListener(item) }
            }
        }
    }

    private fun setEmptyImage(position: Int, view: ImageView) {
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
