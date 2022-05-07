package com.depromeet.baton.presentation.ui.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemAddressSearchBinding
import com.depromeet.baton.presentation.ui.address.model.AddressInfo
import com.depromeet.baton.presentation.ui.address.viewmodel.SearchAddressViewModel

class SearchAddressAdapter (
    private val viewModel: SearchAddressViewModel,
    private val itemClick: (AddressInfo) -> Unit,
    ): ListAdapter<AddressInfo, SearchAddressAdapter.AddressViewHolder>(addressDiffUtil)  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding: ItemAddressSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_address_search,
            parent,
            false
        )
        val viewHolder = AddressViewHolder(binding)
        binding.apply {
           root.setOnClickListener {
                itemClick(getItem(viewHolder.adapterPosition)) //getItem()으로 아이템 가져옴
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder:  AddressViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind( item)
    }

   inner class AddressViewHolder(val binding: ItemAddressSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind( item: AddressInfo) {
            binding.itemAddressRoadTv.text = item.roadAddress
            binding.itemAddressJibunTv.text = item.address
        }
    }

    companion object {
        val addressDiffUtil = object : DiffUtil.ItemCallback<AddressInfo>() {
            override fun areContentsTheSame(oldItem: AddressInfo, newItem: AddressInfo) =
                oldItem.roadAddress == newItem.roadAddress

            override fun areItemsTheSame(oldItem: AddressInfo, newItem: AddressInfo) =
                oldItem ==newItem
        }
    }
}