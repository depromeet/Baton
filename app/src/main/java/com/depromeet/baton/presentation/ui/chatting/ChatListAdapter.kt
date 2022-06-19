package com.depromeet.baton.presentation.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.chat.ChatRoom
import com.depromeet.baton.databinding.ItemChatListBinding
import com.depromeet.baton.util.SimpleDiffUtil


class ChatListAdapter(
    private val clickListener: ((ChatRoom) -> Unit),
) : ListAdapter<ChatRoom, ChatListAdapter.ViewHolder>(SimpleDiffUtil()) {

    inner class ViewHolder(
        private val binding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ChatRoom) {
            with(binding) {


                root.setOnClickListener { clickListener(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}