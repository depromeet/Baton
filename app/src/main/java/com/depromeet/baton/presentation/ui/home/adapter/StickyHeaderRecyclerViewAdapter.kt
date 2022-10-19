package com.depromeet.baton.presentation.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomBinding
import com.depromeet.baton.databinding.FragmentHeaderBinding
import com.depromeet.baton.databinding.FragmentTestBinding
import com.depromeet.baton.databinding.FragmentTopBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.view.FilterChipFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.view.BlankFragment
import com.depromeet.baton.presentation.ui.home.view.HowToUseActivity
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration

class StickyHeaderRecyclerViewAdapter constructor(
    private val filterViewModel: FilterViewModel,
    private val lifeCycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager,
    private val context: Context,
) : ListAdapter<AdapterItem, RecyclerView.ViewHolder>(AdapterItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TOP -> {
                return TopViewHolder(
                    FragmentTopBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HEADER -> {
                return HeaderViewHolder(
                    FragmentHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return BottomViewHolder(
                    FragmentBottomBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), context, filterViewModel, lifeCycleOwner
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (currentList[position].type) {
            TOP -> {
                (holder as TopViewHolder).bind(context)
            }
            HEADER -> {
                (holder as HeaderViewHolder)
            }
            BOTTOM -> {
                (holder as BottomViewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    class TopViewHolder(var binding: FragmentTopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context) {
            binding.btnHomeMore.setOnClickListener {
                HowToUseActivity.start(context)
            }
        }
    }

    class HeaderViewHolder(var binding: FragmentHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    class BottomViewHolder(
        binding: FragmentBottomBinding,
        private val context: Context,
        filterViewModel: FilterViewModel,
        lifeCycleOwner: LifecycleOwner
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
            TicketDetailActivity.start(context, ticketItem.id)
        }

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

private class AdapterItemComparator : DiffUtil.ItemCallback<AdapterItem>() {
    override fun areItemsTheSame(
        oldItem: AdapterItem,
        newItem: AdapterItem
    ): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: AdapterItem,
        newItem: AdapterItem
    ): Boolean {
        return oldItem == newItem
    }
}