package com.depromeet.baton.presentation.ui.ask.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.chat.ChatRoom
import com.depromeet.baton.databinding.ItemChatListBinding
import com.depromeet.baton.databinding.ItemMessageBinding
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.util.SimpleDiffUtil

class MessageitemAdapter (
    private val onClick: (Message) -> Unit,
) : ListAdapter<Message, MessageitemAdapter.ViewHolder>(SimpleDiffUtil()) {

    inner class ViewHolder(
        private val binding: ItemMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Message) {
            with(binding) {
                message =item
                executePendingBindings()
                root.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}