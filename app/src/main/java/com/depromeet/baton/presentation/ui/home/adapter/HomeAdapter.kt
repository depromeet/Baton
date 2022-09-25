package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.FragmentBottomBinding
import com.depromeet.baton.databinding.FragmentFilterChipBinding
import com.depromeet.baton.databinding.FragmentTopBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.view.TopFragment
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration

class HomeAdapter constructor(
    val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM1) {
            return Item1ViewHolder(FragmentTopBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == ITEM2) {
            return Item2ViewHolder(FragmentFilterChipBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return Item3ViewHolder(FragmentBottomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == ITEM1) {
            (holder as Item1ViewHolder).bind()
        } else if (type == ITEM2) {
            (holder as Item2ViewHolder)
        } else if (type == ITEM3) {
            (holder as Item3ViewHolder)
        }
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(context, ticketItem.id)
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            ITEM1
        else if (position == 1)
            ITEM2
        else ITEM3
    }


    inner class Item1ViewHolder(var binding: FragmentTopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }
    }

    inner class Item2ViewHolder(var binding: FragmentFilterChipBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    inner class Item3ViewHolder(var binding: FragmentBottomBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val adapter = TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL,::setTicketItemClickListener)
            binding.rv.adapter = adapter
            val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.rv.layoutManager = gridLayoutManager
            binding.rv.addItemDecoration(TicketItemVerticalDecoration())
            adapter.submitList(events)
        }
    }

    private val events: List<FilteredTicket>
        get() {
            val result = mutableListOf<FilteredTicket>()
            for (i in 0 until 4) {
                result.add(
                    FilteredTicket(
                        i, "f", "f", price = "2444", null,
                        null, null, null, latitude = 3434.33, longitude = 2424.5, null, null
                    )
                )
            }
            return result
        }

    companion object {
        private const val ITEM1 = 1
        private const val ITEM2 = 2
        private const val ITEM3 = 3
    }
}