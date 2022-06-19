package com.depromeet.baton.presentation.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.chat.Message
import com.depromeet.baton.databinding.ItemMyChatBinding
import com.depromeet.baton.databinding.ItemOtherChatBinding
import com.depromeet.baton.util.SimpleDiffUtil
import java.text.SimpleDateFormat
import java.util.*


class ChatMessageAdapter(
    private val auth: Int
) : ListAdapter<Message, RecyclerView.ViewHolder>(SimpleDiffUtil()) {
    private val date = Date(System.currentTimeMillis())
    private val sdf = SimpleDateFormat("yyyy:MM:dd hh:mm:ss aa", Locale("ko", "KR"))
    private val strDate = sdf.format(date)
    private val hour = if (strDate.slice(20..21) == "오후") (strDate.slice(11..12).toInt() + 12).toString() else strDate.slice(11..15)
    private val formattedDate = strDate.slice(20..21) + " " + hour + strDate.slice(13..15)

    inner class MyChatItemViewHolder(private val binding: ItemMyChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Message) {
            binding.apply {
                chatmessageTvMessage.text = chat.message
                chatmessageTvDate.text = formattedDate
            }
        }
    }

    inner class OtherChatItemViewHolder(private val binding: ItemOtherChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Message) {
            binding.apply {
                chatmessageTvMessage.text = chat.message
                chatmessageTvDate.text = formattedDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MY_CHAT) {
            MyChatItemViewHolder(
                ItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            OtherChatItemViewHolder(
                ItemOtherChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MY_CHAT) {
            (holder as MyChatItemViewHolder).bind(currentList[position])
        } else {
            (holder as OtherChatItemViewHolder).bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (auth == currentList[position].senderId)
            MY_CHAT
        else OTHER_CHAT
    }

    companion object {
        private const val MY_CHAT = 1
        private const val OTHER_CHAT = 2
    }
}