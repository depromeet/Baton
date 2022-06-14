package com.depromeet.baton.presentation.ui.mypage.adapter

import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter

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
import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.util.SimpleDiffUtil
import com.depromeet.bds.utils.toPx
import timber.log.Timber


class BookMarkItemRvAdapter(
    private val context: Context,
    private val clickListener: (BookmarkTicket) -> Unit,
    private val clickBookmarkListener: (BookmarkTicket ,Int) -> Unit
) : ListAdapter<BookmarkTicket, BookMarkItemRvAdapter.BookMarkItemViewHolder>(SimpleDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookMarkItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemTicketBinding>(layoutInflater, com.depromeet.baton.R.layout.item_ticket, parent, false)
        return BookMarkItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

   inner class BookMarkItemViewHolder(private val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkTicket, position: Int) {
            with(binding) {
                ticket = FilteredTicket(item.ticket.id, item.ticket.location, item.ticket.address, priceFormat(item.ticket.price.toFloat()), item.ticket.mainImage
                ,item.ticket.tags,item.ticket.remainingDay.toString(),item.ticket.remainingNumber.toString(), item.ticket.latitude, item.ticket.longitude,
                    distanceFormatUtil(item.ticket.distance), item.ticket.type)
                executePendingBindings()

                setLikeBtnClickListener(ctvItemTicketLike)

                if (item.ticket.mainImage==null) setEmptyImage(position, ibtnItemTicket)

                root.setOnClickListener {
                    clickListener(item)
                }
                ctvItemTicketLike.setOnClickListener {
                    ctvItemTicketLike.isChecked=false
                    clickBookmarkListener(item, position)
                    removeItem(position)
                }
            }
        }
    }

    fun removeItem(position : Int){
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
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
}
