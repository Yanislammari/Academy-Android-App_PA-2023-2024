package com.example.academy.views.recycler_view.conversations_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.conversation.ConversationModel
import com.example.academy.views.click_listeners.ConversationOnClickListener

class ConversationListAdapter(private var conversationList: List<ConversationModel>, private val conversationClickHandler: ConversationOnClickListener) : RecyclerView.Adapter<ConversationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val conversationView = LayoutInflater.from(parent.context).inflate(R.layout.conversation_cell, parent, false)
        return ConversationViewHolder(conversationView)
    }

    override fun getItemCount(): Int {
        return this.conversationList.size
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val currentConversationData = this.conversationList[position]
        holder.bind(currentConversationData)

        holder.itemView.setOnClickListener {
            conversationClickHandler.displayConversationMessages(currentConversationData)
        }
    }
}