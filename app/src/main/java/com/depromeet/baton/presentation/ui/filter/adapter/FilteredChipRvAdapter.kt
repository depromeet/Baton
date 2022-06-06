package com.depromeet.baton.presentation.ui.filter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.filter.view.TermFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.search.FilterSearchViewModel
import com.depromeet.bds.component.BdsInputChip


class FilteredChipRvAdapter(
    private val filterViewModel: BaseViewModel,
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
        if(filterViewModel is FilterViewModel)
          when (chip) {
              chip as? TicketKind -> {
                  view.text = chip.value
                  view.setOnClickListener {
                      filterViewModel.setTicketKind(chip)
                  }
              }
              chip as? TradeType -> {
                  view.text = chip.value
                  view.setOnClickListener {
                      filterViewModel.setTradeType(chip)
                  }
              }
              chip as? TransferFee -> {
                  view.text = chip.value
                  view.setOnClickListener {
                      filterViewModel.setTransferFeeType(chip)
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
              else -> {
                  view.text = chip as String
                  view.setOnClickListener {
                      if (view.text.contains("회")) {
                          filterViewModel.setPtTerm(TermFragment.MIN, TermFragment.PT_MAX)
                      }
                      if (view.text.contains("개월")) {
                          filterViewModel.setGymTerm(TermFragment.MIN, TermFragment.GYM_MAX)
                      }
                      if (view.text.contains("원")) {
                          filterViewModel.setPrice(TermFragment.MIN, TermFragment.PRICE_MAX)
                      }
                  }
              }
          }
        if(filterViewModel is FilterSearchViewModel)
            when (chip) {
                chip as? TicketKind -> {
                    view.text = chip.value
                    view.setOnClickListener {
                        filterViewModel.setTicketKind(chip)
                    }
                }
                chip as? TradeType -> {
                    view.text = chip.value
                    view.setOnClickListener {
                        filterViewModel.setTradeType(chip)
                    }
                }
                chip as? TransferFee -> {
                    view.text = chip.value
                    view.setOnClickListener {
                        filterViewModel.setTransferFeeType(chip)
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
                else -> {
                    view.text = chip as String
                    view.setOnClickListener {
                        if (view.text.contains("회")) {
                            filterViewModel.setPtTerm(TermFragment.MIN, TermFragment.PT_MAX)
                        }
                        if (view.text.contains("개월")) {
                            filterViewModel.setGymTerm(TermFragment.MIN, TermFragment.GYM_MAX)
                        }
                        if (view.text.contains("원")) {
                            filterViewModel.setPrice(TermFragment.MIN, TermFragment.PRICE_MAX)
                        }
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


