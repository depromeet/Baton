package com.depromeet.baton.presentation.ui.detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemDetailImageBinding


class TicketImgRvAdapter(): ListAdapter<String, TicketImgRvAdapter.ImgViewHolder>(diffUtil) {

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
            binding.itemDetailimgIv.setImageURI(Uri.parse(item))
        }
    }
    companion object{
        val diffUtil = object :DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}