package com.depromeet.baton.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddressBinding.inflate
import com.depromeet.baton.databinding.ItemBottomsheetCheckBinding
import com.depromeet.baton.databinding.ItemPrimaryOutlineTagBinding
import com.depromeet.baton.databinding.ItemPrimaryTagBinding
import com.depromeet.baton.domain.model.BatonHashTag

class TicketTagAdapter<B : ViewDataBinding>(
    private val layout : Int,
    private val tagList : List<BatonHashTag>) : RecyclerView.Adapter<TicketTagAdapter<B>.TicketTagViewHolder<B>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketTagViewHolder<B>{
        val binding :B = DataBindingUtil.inflate(LayoutInflater.from(parent.context),layout, parent,  false)
        return  TicketTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketTagViewHolder<B>, position: Int) {
        val item = tagList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return tagList.size
    }

    inner class TicketTagViewHolder<B :ViewDataBinding> (val binding: B): RecyclerView.ViewHolder(binding.root){
        fun bind(item : BatonHashTag){
            when(binding){
                is ItemPrimaryTagBinding->{
                    binding.primaryTag.setText(item.displayTitle)
                }
                is ItemPrimaryOutlineTagBinding->{
                    binding.primaryLineTag.setText(item.displayTitle)
                }
            }
        }

    }

}