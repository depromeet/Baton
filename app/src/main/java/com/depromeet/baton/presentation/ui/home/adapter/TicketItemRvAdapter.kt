package com.depromeet.baton.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.util.SimpleDiffUtil
import com.depromeet.bds.utils.toPx


class TicketItemRvAdapter(
    private val scrollType: String,
    private val clickListener: (FilteredTicket) -> Unit,
    private val bookMarkDeleteClickListener: (FilteredTicket) -> Unit,
    private val bookMarkAddClickListener: (FilteredTicket) -> Unit
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

                //거리
                tvItemTicketDistance.text = distanceFormatUtil(item.distance!!.toDouble())

                //좋아요 버튼 todo 서버연결
                ctvItemTicketLike.visibility = View.INVISIBLE
                // setLikeBtnClickListener(ctvItemTicketLike, item)

                //엠티뷰
                setEmptyImage(item.type ?: "기타", ivItemEmpty)

                //상세페이지로
                root.setOnClickListener { clickListener(item) }
            }
        }
    }

    private fun setEmptyImage(type: String, view: ImageView) {
        when (type) {
            "헬스" -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_health_86)
            "기타" -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_etc_86)
            "PT" -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pt_86)
            "필라테스" -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pilates_86)
        }
    }

    private fun setLikeBtnClickListener(view: CheckedTextView, item: FilteredTicket) {
        view.setOnClickListener {
            if (view.isChecked) bookMarkAddClickListener(item)
            else bookMarkDeleteClickListener(item)
            view.toggle()
        }
    }

    companion object {
        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
    }
}
