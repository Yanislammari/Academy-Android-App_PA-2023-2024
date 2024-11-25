package com.example.academy.views.recycler_view.conversation_messages_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.message.MessageModel

class ConversationMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var messageText: TextView = itemView.findViewById(R.id.message_text)

    fun bind(message: MessageModel) {
        this.messageText.text = message.message
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): ConversationMessageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return ConversationMessageViewHolder(view)
        }
    }
}
