package com.depromeet.baton.presentation.ui.address

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemSearchShopBinding
import com.depromeet.baton.map.domain.entity.LocationEntity
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.ShopInfo
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel

class SearchShopRvAdapter(
    private val writePostViewModel: WritePostViewModel,
) :
    ListAdapter<ShopEntity, SearchShopRvAdapter.SearchShopViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShopViewHolder {
        val binding: ItemSearchShopBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_shop, parent, false)
        return SearchShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchShopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchShopViewHolder(val binding: ItemSearchShopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShopEntity) {
            with(binding) {
                tvItemSearchShopName.text = item.name
                tvItemSearchShopAddress.text = item.name
                Log.e("ㅡㅡㅡㅡㅡㅡshopInfoListㅡㅡㅡㅡ",item.name)
                tvItemSearchShopAddressSelect.setOnClickListener {
                    writePostViewModel.setSelectShop(ShopInfo(tvItemSearchShopName.text.toString(), tvItemSearchShopAddress.text.toString()))
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ShopEntity>() {
            override fun areContentsTheSame(oldItem: ShopEntity, newItem: ShopEntity) =
                oldItem.name == newItem.name

            override fun areItemsTheSame(oldItem: ShopEntity, newItem: ShopEntity) =
                oldItem.name == newItem.name
        }
    }
}