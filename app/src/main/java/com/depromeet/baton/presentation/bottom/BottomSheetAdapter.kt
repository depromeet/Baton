package com.depromeet.baton.presentation.bottom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemBottomsheetBinding
import timber.log.Timber

class BottomSheetAdapter(private val itemClick: (Int) -> Unit): ListAdapter< CheckItem<String>, BottomSheetAdapter.ViewHolder>(
    DiffUtil
){
    var oldCheckedPos :Int?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBottomsheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_bottomsheet,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        Timber.e("create")
        binding.apply {
            root.setOnClickListener {
              //  binding.itemBottomsheetCheckIv.visibility = View.VISIBLE
                itemClick(viewHolder.adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }


    inner class ViewHolder(val binding:  ItemBottomsheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind( item: CheckItem<String>, pos : Int) {
            Timber.e(item.toString()+"bind")
            if(item.isChecked ==true) binding.itemBottomsheetCheckIv.visibility=View.VISIBLE
            else binding.itemBottomsheetCheckIv.visibility=View.INVISIBLE

            binding.itemBottomsheetTv.text = item.data
        }
    }

    companion object {
        val DiffUtil = object : DiffUtil.ItemCallback<CheckItem<String>>() {
            override fun areContentsTheSame(oldItem: CheckItem<String>, newItem: CheckItem<String>):Boolean{
                return  oldItem.isChecked == newItem.isChecked && oldItem.data == newItem.data
            }

            override fun areItemsTheSame(oldItem: CheckItem<String>, newItem: CheckItem<String>) :Boolean{
                return oldItem.isChecked == newItem.isChecked && oldItem.data == newItem.data
            }

        }
    }
}