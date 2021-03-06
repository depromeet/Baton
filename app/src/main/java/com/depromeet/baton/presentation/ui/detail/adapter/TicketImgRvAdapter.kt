package com.depromeet.baton.presentation.ui.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemDetailImageBinding
import com.depromeet.baton.util.SimpleDiffUtil


class TicketImgRvAdapter(private val context: Context): ListAdapter<String, TicketImgRvAdapter.ImgViewHolder>(SimpleDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        val binding: ItemDetailImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_detail_image,
            parent,
            false
        )
        return ImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind( item)
    }

    inner class ImgViewHolder(val binding : ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String){
            Glide.with(context)
                .load(item)
                .into(binding.itemDetailimgIv)
        }
    }
}