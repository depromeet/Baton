package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.*
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.view.FilterChipFragment
import com.depromeet.baton.presentation.ui.home.view.BlankFragment
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.util.SimpleDiffUtil

data class AdapterItem(var type: Int, val isHeader:Boolean)

class StickyHeaderRecyclerViewAdapter constructor(
    val fragmentManager:FragmentManager,
    val context: Context,
)  : ListAdapter<AdapterItem, RecyclerView.ViewHolder>(SimpleDiffUtil()) {
var count=1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e("+++onCreateViewHolder: ", "$count 번째 생성")
      count+=1
        return if (viewType == TOP) {
            return Item1ViewHolder(FragmentTopBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == HEADER) {
            return Item2ViewHolder(FragmentHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return Item3ViewHolder(FragmentBottomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (currentList[position].type==TOP) {
            (holder as Item1ViewHolder).bind()
        } else if (currentList[position].type== HEADER) {
            (holder as Item2ViewHolder)
        } else if (currentList[position].type== BOTTOM) {
            (holder as Item3ViewHolder)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        Log.e("+++onViewRecycled", "onViewRecycled: ${holder.bindingAdapterPosition}")
        super.onViewRecycled(holder)
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(context, ticketItem.id)
    }


    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }


    inner class Item1ViewHolder(var binding: FragmentTopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }
    }

    inner class Item2ViewHolder(var binding: FragmentHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
      //     fragmentManager.beginTransaction().add(R.id.fcv2,FilterChipFragment(),"hi").commit()
        }
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
                        i, "서울시 영등포구 당산동", "서울시 영등포구 당산동", price = "13000", null,
                        null, null, "30", latitude = 3434.33, longitude = 2424.5, null, null
                    )
                )
            }
            return result
        }


   lateinit  var binding:FragmentTestBinding
    fun getHeaderView(list: RecyclerView, position: Int): View? {
      binding= FragmentTestBinding.inflate(LayoutInflater.from(list.context), list, false)
        return if(currentList[position].type== HEADER || currentList[position].type== BOTTOM){

            fragmentManager.beginTransaction().replace(R.id.test_fcv,FilterChipFragment()).commit()
            binding.mtHomeTitle.visibility=View.GONE
            binding.rvHome.visibility=View.GONE
            binding.root

        } else {
            fragmentManager.beginTransaction().replace(R.id.test_fcv,BlankFragment()).commit()
            null
        }
    }

    companion object {
         const val TOP = 1
         const val HEADER = 2
         const val BOTTOM = 3
    }
}