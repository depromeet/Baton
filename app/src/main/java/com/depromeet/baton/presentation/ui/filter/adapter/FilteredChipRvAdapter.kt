package com.depromeet.baton.presentation.ui.filter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.domain.model.AdditionalOptions
import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.domain.model.TransactionMethod
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.bds.component.BdsInputChip


class FilteredChipRvAdapter(
    private val filterViewModel: FilterViewModel,
    private val context: Context
) : ListAdapter<Any, FilteredChipRvAdapter.FilteredChipViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredChipViewHolder {
        return FilteredChipViewHolder(BdsInputChip(context))
    }

    override fun onBindViewHolder(holder: FilteredChipRvAdapter.FilteredChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FilteredChipViewHolder(private val view: BdsInputChip) : RecyclerView.ViewHolder(view) {
        fun bind(chip: Any) {
            setFilteredChipClickListener(chip, view)
        }
    }

    private fun setFilteredChipClickListener(chip: Any, view: BdsInputChip) {
        when (chip) {
            chip as? TicketKind -> {
                view.text = chip.value
                view.setOnClickListener {
                    filterViewModel.setTicketKind(chip)
                }
            }
            chip as? TransactionMethod -> {
                view.text = chip.value
                view.setOnClickListener {
                    filterViewModel.setTransactionMethod(chip)
                }
            }
            chip as? AdditionalOptions -> {
                view.text = chip.value
                view.setOnClickListener {
                    filterViewModel.setAdditionalOptions(chip)
                }

            }
            chip as? HashTag -> {
                view.text = chip.value
                view.setOnClickListener {
                    filterViewModel.setHashTag(chip)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean =
                oldItem == newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean =
                oldItem == newItem
        }
    }
}
