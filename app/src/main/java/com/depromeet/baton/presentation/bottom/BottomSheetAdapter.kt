package com.depromeet.baton.presentation.bottom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemBottomsheetBinding
import com.depromeet.baton.databinding.ItemBottomsheetCheckBinding

class BottomSheetAdapter<B : ViewDataBinding>(
    private val layout: Int,
    private val itemClick: (Int) -> Unit
) : ListAdapter<  CheckItem<String>, BottomSheetAdapter<B>.BottomSheetItemViewHolder<B>>
    (DiffUtil) {
    var oldCheckedPos: Int? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetItemViewHolder<B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(layoutInflater, layout, parent, false)

        return BottomSheetItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetItemViewHolder<B>, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }


    inner class BottomSheetItemViewHolder<B : ViewDataBinding>(private val binding: B) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CheckItem<String>, pos : Int) {
            when (binding) {
                //checklayout
                is ItemBottomsheetCheckBinding -> {

                    binding.root.setOnClickListener {
                        itemClick(pos)
                        binding.itemBottomsheetTv.isEnabled = true
                    }

                    if (item.isChecked == true){
                        binding.itemBottomsheetCheckIv.visibility = View.VISIBLE
                        binding.itemBottomsheetTv.isEnabled=true
                    }
                    else {
                        binding.itemBottomsheetCheckIv.visibility = View.GONE
                        binding.itemBottomsheetTv.isEnabled = false
                    }
                    binding.itemBottomsheetTv.text = item.data

                }
                //일반 레이아웃
                is ItemBottomsheetBinding -> {
                    binding.root.setOnClickListener {
                        itemClick(pos)
                    }
                    binding.itemBottomsheetTv.text = item.data

                }
            }
        }
    }

    companion object {
        val DiffUtil = object : DiffUtil.ItemCallback<CheckItem<String>>() {
            override fun areContentsTheSame(
                oldItem: CheckItem<String>,
                newItem: CheckItem<String>
            ): Boolean {
                return oldItem.isChecked == newItem.isChecked && oldItem.data == newItem.data
            }

            override fun areItemsTheSame(
                oldItem: CheckItem<String>,
                newItem: CheckItem<String>
            ): Boolean {
                return oldItem.isChecked == newItem.isChecked && oldItem.data == newItem.data
            }

        }
    }
}