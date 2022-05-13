package com.depromeet.baton.presentation.ui.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemSearchShopBinding
import com.depromeet.baton.presentation.ui.writepost.viewmodel.ShopInfo
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel

class SearchShopRvAdapter(
    private val writePostViewModel: WritePostViewModel,
) :
    ListAdapter<ShopInfo, SearchShopRvAdapter.SearchShopViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShopViewHolder {
        val binding: ItemSearchShopBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_shop, parent, false)
        return SearchShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchShopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchShopViewHolder(val binding: ItemSearchShopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShopInfo) {
            with(binding) {
                tvItemSearchShopName.text = item.shopName
                tvItemSearchShopAddress.text = item.shopAddress
                tvItemSearchShopAddressSelect.setOnClickListener {
                    writePostViewModel.setSelectShop(ShopInfo(tvItemSearchShopName.text.toString(), tvItemSearchShopAddress.text.toString()))
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ShopInfo>() {
            override fun areContentsTheSame(oldItem: ShopInfo, newItem: ShopInfo) =
                oldItem.shopName == newItem.shopName

            override fun areItemsTheSame(oldItem: ShopInfo, newItem: ShopInfo) =
                oldItem.shopName == newItem.shopName
        }
    }
}