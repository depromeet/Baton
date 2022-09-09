package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.*
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.view.FilterChipFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.view.BlankFragment
import com.depromeet.baton.presentation.ui.home.view.TestFragment
import com.depromeet.baton.presentation.ui.home.view.TopFragment
import com.depromeet.baton.presentation.ui.search.view.SearchFragment
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration

data class AdapterItem(var type: Int, val isHeader:Boolean)

class RecyclerView2Adapter constructor(
    val fragmentManager:FragmentManager,

    val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val recyclerItemList: ArrayList<AdapterItem> = ArrayList()

    init{
            recyclerItemList.add(AdapterItem(ITEM1,false))
            recyclerItemList.add(AdapterItem(ITEM2,true))
              for (data in 0..50) {
                    recyclerItemList.add(AdapterItem(ITEM3,false))
                }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM1) {
            return Item1ViewHolder(FragmentTopBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == ITEM2) {
            return Item2ViewHolder(FragmentHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return Item3ViewHolder(FragmentBottomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (recyclerItemList[position].type==ITEM1) {
            (holder as Item1ViewHolder).bind()
        } else if (recyclerItemList[position].type==ITEM2) {
            (holder as Item2ViewHolder)
        } else if (recyclerItemList[position].type==ITEM3) {
            (holder as Item3ViewHolder)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        Log.e("onViewRecycled", "onViewRecycled: ${holder.bindingAdapterPosition}")
        super.onViewRecycled(holder)
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(context, ticketItem.id)
    }

    override fun getItemCount(): Int {
        return recyclerItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return recyclerItemList[position].type
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


    fun isHeader(position: Int) = recyclerItemList[position].isHeader


   lateinit  var binding:FragmentTestBinding
    fun getHeaderView(list: RecyclerView, position: Int): View? {
         binding= FragmentTestBinding.inflate(LayoutInflater.from(list.context), list, false)
        if(recyclerItemList[position].type==ITEM2 || recyclerItemList[position].type==ITEM3){

           // LayoutInflater.from(context).inflate(R.layout.fragment_header2, null)
            Log.e("ㅡ-ㅡ", "getHeaderView : 트루")
            fragmentManager.beginTransaction().replace(R.id.test_fcv,FilterChipFragment(),"hi").commit()
        binding.mtHomeTitle.visibility=View.GONE

            binding.rvHome.visibility=View.GONE
            binding.testFcv.visibility=View.GONE
        return  binding.root

        }
       else {
            fragmentManager.beginTransaction().replace(R.id.test_fcv,BlankFragment(),"hi").commit()
            return  null

       }
    }

init{
   // LayoutInflater.from(context).inflate(R.layout.fragment_header2, null)
}

    companion object {
        private const val ITEM1 = 1
        private const val ITEM2 = 2
        private const val ITEM3 = 3
    }
}