package com.depromeet.baton.presentation.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
) : ListAdapter<FilteredTicket, TicketItemRvAdapter.TicketItemViewHolder>(SimpleDiffUtil()) {

    private lateinit var inflater: LayoutInflater

    override fun onViewRecycled(holder: TicketItemViewHolder) {
        Log.e("ㅡㅡㅡonViewRecycled", holder.bindingAdapterPosition.toString())
        super.onViewRecycled(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        Log.e("ㅡㅡㅡonCreateViewHolder", "onCreateViewHolder")
     //   if (!::inflater.isInitialized)
     //       inflater = LayoutInflater.from(parent.context)

        val binding = ItemTicketBinding.inflate( LayoutInflater.from(parent.context), parent, false)

        //이미지 라운드 처리
     //   binding.ibtnItemTicket.clipToOutline = true

        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        return holder.bind(currentList[position], scrollType, clickListener)
    }

    class TicketItemViewHolder(private val binding: ItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilteredTicket, scrollType: String, clickListener: (FilteredTicket) -> Unit) {
            with(binding) {
                ticket = item
                executePendingBindings()

                //가로스크롤뷰
                if (scrollType == SCROLL_TYPE_HORIZONTAL) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.width = 156.toPx()
                    ctlItemTicketContainer.layoutParams = lp
                }

                //태그
                if ((item.tags?.size ?: 0) > 2) {
                    val etcSize = (item.tags?.size ?: 0) - 2
                    itemTicketTagEtc.text = "+$etcSize"
                }

                //거리
//                tvItemTicketDistance.text = distanceFormatUtil(item.distance!!.toDouble())

                //좋아요 버튼 todo 서버연결
                ctvItemTicketLike.visibility = View.INVISIBLE

                //엠티뷰
                setEmptyImage(item.type ?: "기타", ivItemEmpty)

                //상세페이지로
                root.setOnClickListener { clickListener(item) }
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
    }

    companion object {
        const val SCROLL_TYPE_VERTICAL = "VERTICAL"
        const val SCROLL_TYPE_HORIZONTAL = "HORIZONTAL"
    }
}
