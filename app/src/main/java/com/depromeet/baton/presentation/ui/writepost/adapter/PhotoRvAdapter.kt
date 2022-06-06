package com.depromeet.baton.presentation.ui.writepost.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemPhotoBinding
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.bds.utils.toPx


class PhotoRvAdapter(
    private val viewModel: WritePostViewModel,
    private val context: Context
) : ListAdapter<Uri, PhotoRvAdapter.PhotoViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding: ItemPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_photo, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri, position: Int) {
            if (position == 0) binding.ctlItemPhotoRepresentation.visibility = View.VISIBLE
            Glide.with(context)
                .load(item)
                .transform(CenterCrop(), RoundedCorners(8.toPx()))
                .into(binding.ivItemPhoto)

            binding.ivItemPhotoCancle.setOnClickListener {
                viewModel.deleteImg(position)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Uri, newItem: Uri) =
                oldItem == newItem

        }
    }
}
