package com.depromeet.baton.presentation.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.chat.Message
import com.depromeet.baton.databinding.ItemReceiverChatBinding
import com.depromeet.baton.databinding.ItemSenderChatBinding
import com.depromeet.baton.util.SimpleDiffUtil
import java.text.SimpleDateFormat
import java.util.*


class ChatMessageAdapter(
    private val userId: Int,
    private val receiverProfileImg: String
) : ListAdapter<Message, RecyclerView.ViewHolder>(SimpleDiffUtil()) {

    inner class SenderChatItemViewHolder(private val binding: ItemSenderChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Message) {
            binding.apply {
                chatmessageTvMessage.text = chat.message
                chatmessageTvDate.text = getChatTime() //todo 일단 현재시간
            }
        }
    }

    inner class ReceiverChatItemViewHolder(private val binding: ItemReceiverChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Message) {
            binding.apply {
                profileImg = receiverProfileImg
                chatmessageTvMessage.text = chat.message
                chatmessageTvDate.text = getChatTime()  //todo 일단 현재시간
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_CHAT) {
            SenderChatItemViewHolder(
                ItemSenderChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            ReceiverChatItemViewHolder(
                ItemReceiverChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == SENDER_CHAT) {
            (holder as SenderChatItemViewHolder).bind(currentList[position])
        } else {
            (holder as ReceiverChatItemViewHolder).bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (userId == currentList[position].senderId)
            SENDER_CHAT
        else RECEIVER_CHAT
    }

    private fun getChatTime(): String {
        val date = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("yyyy:MM:dd hh:mm:ss aa", Locale("ko", "KR"))
        val strDate = simpleDateFormat.format(date)
        val hour = if (strDate.slice(20..21) == "오후") (strDate.slice(11..12).toInt() + 12).toString() else strDate.slice(11..12)
        return strDate.slice(20..21) + " " + hour + strDate.slice(13..15)
    }

    companion object {
        private const val SENDER_CHAT = 1
        private const val RECEIVER_CHAT = 2
    }
}