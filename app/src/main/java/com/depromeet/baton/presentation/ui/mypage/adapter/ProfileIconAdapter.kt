package com.depromeet.baton.presentation.ui.mypage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.databinding.ItemProfileCameraBinding
import com.depromeet.baton.databinding.ItemProfileIconBinding
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIconItem
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel

class ProfileIconAdapter(
    private val viewModel: ProfileViewModel,
    private val initList : ArrayList<ProfileIconItem>,
    private val cameraClick: () -> Unit= {},
    private val itemClick: (ProfileIcon , Int) -> Unit
) : ListAdapter<ProfileIconItem, ProfileIconAdapter.ProfileViewHolder>(diffCallback) {

    var beforeChecked = -1

    fun toggle(position: Int):List<ProfileIconItem>{
        val toggleList = ArrayList<ProfileIconItem> ()
        toggleList.addAll(initList.map { it.clone() })
        toggleList[position].isChecked=true
        if(beforeChecked!=-1) toggleList[beforeChecked].isChecked=false
        beforeChecked = position
        return toggleList
    }

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType){
            ProfileIconItem.EmotionIcon.VIEW_TYPE->{
                val binding = DataBindingUtil.inflate<ItemProfileIconBinding>(layoutInflater, viewType, parent, false)
                ProfileIconViewHolder(binding)
            }
            ProfileIconItem.CameraIcon.VIEW_TYPE->{
                val binding = DataBindingUtil.inflate<ItemProfileCameraBinding>(layoutInflater, viewType, parent, false)
                ProfileCameraViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Cannot create ViewHolder for view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    abstract class ProfileViewHolder( itemView: ViewDataBinding
    ) : RecyclerView.ViewHolder(itemView.root) {
        abstract fun bind(item: ProfileIconItem?, position: Int)
    }

    inner class ProfileIconViewHolder(private val binding : ItemProfileIconBinding):
        ProfileViewHolder(binding){
        override fun bind(item: ProfileIconItem?, position: Int){
            item!!.icon?.let { binding.profileIconBtn.setImageResource(it.size40) }
            binding.profileIconBtn.setOnClickListener {
                binding.profileIconBtn.isSelected= !binding.profileIconBtn.isSelected
                onClickImage(position, item)
            }
            binding.profileIconBtn.isSelected= getItem(position).isChecked
        }
    }

    inner class ProfileCameraViewHolder(private val binding : ItemProfileCameraBinding):
        ProfileViewHolder(binding){
        override fun bind(item: ProfileIconItem?, position: Int) {
            binding.profileCamera.setOnClickListener { cameraClick }
        }
    }

    fun onClickImage(position: Int, item : ProfileIconItem){
        itemClick(item.icon!!, position)
        submitList(toggle(position).toList())
        viewModel.onClickImage(item.icon!!)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ProfileIconItem>() {
            override fun areItemsTheSame(
                oldItem: ProfileIconItem,
                newItem: ProfileIconItem
            ): Boolean {
               return oldItem.isChecked == newItem.isChecked &&oldItem==newItem
            }

            override fun areContentsTheSame(
                oldItem: ProfileIconItem,
                newItem: ProfileIconItem
            ): Boolean {
                return oldItem.isChecked == newItem.isChecked
            }

        }

    }
}