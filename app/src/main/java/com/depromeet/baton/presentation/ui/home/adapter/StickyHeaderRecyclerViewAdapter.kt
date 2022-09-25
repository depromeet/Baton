package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.*
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.view.FilterChipFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.view.BlankFragment
import com.depromeet.baton.presentation.ui.home.view.HowToUseActivity
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.util.SimpleDiffUtil


data class AdapterItem(var type: Int, val isHeader: Boolean)

class StickyHeaderRecyclerViewAdapter constructor(
    val filterViewModel: FilterViewModel,
    val lifeCycleOwner: LifecycleOwner,
    val fragmentManager: FragmentManager,
    val context: Context,
) : ListAdapter<AdapterItem, RecyclerView.ViewHolder>(SimpleDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TOP) {
            return TopViewHolder(
                FragmentTopBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == HEADER) {
            return HeaderViewHolder(
                FragmentHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return BottomViewHolder(
                FragmentBottomBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position].type == TOP) {
            (holder as TopViewHolder).bind()
        } else if (currentList[position].type == HEADER) {
            (holder as HeaderViewHolder)
        } else if (currentList[position].type == BOTTOM) {
            (holder as BottomViewHolder)
        }
    }

    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(context, ticketItem.id)
    }


    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    inner class TopViewHolder(var binding: FragmentTopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.btnHomeMore.setOnClickListener {
                Log.e("ㅡㅡㅡㄷㄱ","ㅎㅇ")
                HowToUseActivity.start(context)
            }
        }
    }

    inner class HeaderViewHolder(var binding: FragmentHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    inner class BottomViewHolder(var binding: FragmentBottomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val adapter = TicketItemRvAdapter(
                TicketItemRvAdapter.SCROLL_TYPE_VERTICAL,
                ::setTicketItemClickListener
            )
            binding.rv.adapter = adapter
            val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.rv.layoutManager = gridLayoutManager
            binding.rv.addItemDecoration(TicketItemVerticalDecoration())

            filterViewModel.filteredTicketList.observe(lifeCycleOwner) { list ->
                adapter.submitList(list)
            }
        }
    }

    lateinit var binding: FragmentTestBinding
    fun getHeaderView(list: RecyclerView, position: Int): View? {
        binding = FragmentTestBinding.inflate(LayoutInflater.from(list.context), list, false)
        return if (currentList[position].type == HEADER || currentList[position].type == BOTTOM) {
            fragmentManager.beginTransaction().replace(R.id.test_fcv, FilterChipFragment()).commit()
            binding.mtHomeTitle.visibility = View.GONE
            binding.rvHome.visibility = View.GONE
            binding.root
        } else {
            fragmentManager.beginTransaction().replace(R.id.test_fcv, BlankFragment()).commit()
            null
        }
    }

    companion object {
        const val TOP = 1
        const val HEADER = 2
        const val BOTTOM = 3
    }
}