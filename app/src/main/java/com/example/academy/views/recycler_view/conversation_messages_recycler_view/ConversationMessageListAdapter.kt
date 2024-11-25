package com.example.academy.views.recycler_view.conversation_messages_recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.message.MessageModel
import com.example.academy.utils.singleton.DataLayerSingleton

class ConversationMessageListAdapter(private var messageList: List<MessageModel>) : RecyclerView.Adapter<ConversationMessageViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]

        return if(message.sender == DataLayerSingleton.userConnected?.id) {
            R.layout.message_sent_cell
        }
        else {
            R.layout.message_received_cell
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationMessageViewHolder {
        return ConversationMessageViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ConversationMessageViewHolder, position: Int) {
        val currentMessageData = messageList[position]
        holder.bind(currentMessageData)
    }

    fun updateData(newMessages: List<MessageModel>) {
        messageList = newMessages
        notifyDataSetChanged()
    }
}
