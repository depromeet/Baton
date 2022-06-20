package com.depromeet.baton.presentation.ui.mypage.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.databinding.ItemTicketBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.util.distanceFormatUtil
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.util.SimpleDiffUtil
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
               Timber.e("${item.ticket.mainImage} ${item.ticket.type}")
               val badge =  when(TicketKind.valueOf(item.ticket.type).ordinal){
                    0 -> "헬스"
                    1-> "PT"
                    2-> "필라테스"
                    else-> "ETC"
                }
                val remainDay=  if(item.ticket.remainingDay==null) null else item.ticket.remainingDay.toString()
                val remainNumber=  if(item.ticket.remainingNumber==null) null else item.ticket.remainingNumber.toString()

                ticket = FilteredTicket(item.ticket.id, item.ticket.location, item.ticket.address, priceFormat(item.ticket.price.toFloat()), item.ticket.mainImage
                    ,item.ticket.tags,remainDay, remainNumber, item.ticket.latitude, item.ticket.longitude,
                    item.ticket.distance.toInt().toString(), badge)
                executePendingBindings()

                ctvItemTicketLike.isChecked=true
                setLikeBtnClickListener(ctvItemTicketLike)

                if (item.ticket.mainImage==null) setEmptyImage(item.ticket.type, ivItemEmpty)

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

    private fun setEmptyImage(type : String ,view:ImageView) {
        when (type) {
            TicketKind.HEALTH.name -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_health_86)
            TicketKind.PT.name-> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pt_86)
            TicketKind.PILATES_YOGA.name-> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_pilates_86)
            TicketKind.ETC.name -> view.setImageResource(com.depromeet.bds.R.drawable.ic_empty_etc_86)
        }
    }


    private fun setLikeBtnClickListener(view: CheckedTextView) {
        view.setOnClickListener {
            view.toggle()
        }
    }
}
