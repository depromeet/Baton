package com.depromeet.baton.presentation.ui.chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.chat.Message
import com.depromeet.baton.databinding.ItemChatReceiverBinding
import com.depromeet.baton.databinding.ItemChatSendBinding


class ChatMessageAdapter(items: ArrayList<Message>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var context: Context
    var items: ArrayList<Message> = ArrayList<Message>()
    private val mProfileUid: String = "0" //내 uid


    /*그 후 onCreateViewHolder() 에서 getItemViewType 에서 나눴던 것처럼
    뷰타입번호에 리턴값에 따라 각각에 알맞은 뷰에 inflate해주면 된다.
    난 0 ,1 로 리턴했었으니 다음과 같이 switch문을 분기하였다.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            0 -> { //내 채팅인경우
                val binding = ItemChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ChatMessageViewHolder2(binding)
            }
            1 -> { //타인 채팅인 경우
                val binding = ItemChatSendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ChatMessageViewHolder(binding)
            }
        }
        val binding = ItemChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatMessageViewHolder2(binding)
    }

    //값을 구붑ㄴ해 알맞는 홀더에 세팅해줌
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model: Message = items[position]
        if (!model.senderId.equals(mProfileUid)) { //내 메세지인 경우
            val holder2 = holder as ChatMessageViewHolder2
            return holder2.bind(model, position)
        } else {
            val holder1 = holder as ChatMessageViewHolder
            return holder1.bind(model, position)
        }
    }

    //xml을 여러개 사용할 경우 이걸 꼭 오버라이딩해야함
    //getItemViewType()에서 비교해주고 뷰 종류를 2가지 사용할거면 경우에따라 두가지분류로 리턴해주면 된다.
    override fun getItemViewType(position: Int): Int {
        val chatMessage: Message = items[position]
        return if (!chatMessage.senderId.equals(mProfileUid)) { //내 아이디인 경우 오른쪽 뷰로 분기
            0
        } else { //다른사람 아이디면 왼쪽뷰로 분기
            1
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //뷰홀더도 두개가 필요함->타인 채팅 뷰홀더
    inner class ChatMessageViewHolder(private val binding: ItemChatSendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message, position: Int) {
            with(binding) {

            }
        }
    }

    //뷰들을 바인딩 해줍니다. -> 내 뷰홀더
    inner class ChatMessageViewHolder2(private val binding: ItemChatReceiverBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message, position: Int) {
            with(binding) {

            }
        }
    }

    //아이템을 추가해주고싶을때 이거쓰면됨
    fun addItem(item: Message) {
        items.add(item)
    }

    //한꺼번에 추가해주고싶을떄
    fun addItems(items: ArrayList<Message>) {
        this.items = items
    }

    //아이템 전부 삭제
    fun clear() {
        items.clear()
    }

    init {
        this.context = context
        addItems(items)
    }
}