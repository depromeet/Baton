package com.depromeet.baton.presentation.ui.address

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemAddressSearchBinding
import com.depromeet.baton.presentation.ui.address.model.AddressInfo
import timber.log.Timber


class SearchAddressAdapter(
    private val itemClick: (AddressInfo) -> Unit,
    ): ListAdapter<AddressInfo, SearchAddressAdapter.AddressViewHolder>(addressDiffUtil)  {

    var nowQueryListener : SearchColorListener? = null

    interface SearchColorListener{
        fun getQuery():String
    }

    fun setQueryListener (queryListener: SearchColorListener){
        nowQueryListener = queryListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding: ItemAddressSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_address_search,
            parent,
            false
        )

        val viewHolder = AddressViewHolder(binding)
        binding.apply {
            itemAddressSelectBtn.setOnClickListener {
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
            val nowQuery =nowQueryListener?.getQuery()
            binding.apply {
                itemAddressRoadTv.text = item.roadAddress
                itemAddressJibunTv.text = item.address
                if(nowQuery!=null && nowQuery!="") {
                    val start= itemAddressJibunTv.text.indexOf(nowQuery)
                    val startRoad = itemAddressRoadTv.text.indexOf(nowQuery)
                    if(start!=-1){
                        val spJibun = SpannableStringBuilder(itemAddressJibunTv.text)
                        spJibun.setSpan(ForegroundColorSpan(Color.parseColor("#0066FF")),start,start+nowQuery.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        itemAddressJibunTv.text=spJibun

                    }
                    if(startRoad!=-1){
                        val spRoad = SpannableStringBuilder(itemAddressRoadTv.text)
                        spRoad.setSpan(ForegroundColorSpan(Color.parseColor("#0066FF")),startRoad,startRoad+nowQuery.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        itemAddressRoadTv.text= spRoad
                    }
                }
            }
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