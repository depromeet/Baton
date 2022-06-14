package com.depromeet.baton.presentation.ui.address

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemSearchShopBinding
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.ShopInfo
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.util.SimpleDiffUtil

class SearchShopRvAdapter(
    private val writePostViewModel: WritePostViewModel
) :
    ListAdapter<ShopEntity, SearchShopRvAdapter.SearchShopViewHolder>(SimpleDiffUtil()) {

    var nowQueryListener: SearchColorListener? = null

    interface SearchColorListener {
        fun getQuery(): String
    }

    fun setQueryListener(queryListener: SearchColorListener) {
        nowQueryListener = queryListener
    }


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
                tvItemSearchShopAddress.text = item.location.address.address

                val nowQuery = nowQueryListener?.getQuery()
                if (nowQuery != null && nowQuery != "") {
                    val start = tvItemSearchShopName.text.indexOf(nowQuery)
                    if (start != -1) {
                        val spJibun = SpannableStringBuilder(tvItemSearchShopName.text)
                        spJibun.setSpan(ForegroundColorSpan(Color.parseColor("#0066FF")), start, start + nowQuery.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        tvItemSearchShopName.text = spJibun

                    }
                }

                tvItemSearchShopAddressSelect.setOnClickListener {
                    writePostViewModel.setSelectShop(ShopInfo(item.name, item.location.address.address, item.location.latitude, item.location.longitude))
                }
            }
        }
    }
}