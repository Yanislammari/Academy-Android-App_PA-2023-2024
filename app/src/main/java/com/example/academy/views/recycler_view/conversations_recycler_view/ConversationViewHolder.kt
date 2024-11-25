package com.example.academy.views.recycler_view.conversations_recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.conversation.ConversationModel

class ConversationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var conversationUserName: TextView = itemView.findViewById(R.id.conversation_user_name_text_view)
    private var conversationLastMessage: TextView = itemView.findViewById(R.id.conversation_last_message_text_view)
    private var conversationUserProfilePicture: ImageView = itemView.findViewById(R.id.conversation_user_profile_picture)

    fun bind(conversation: ConversationModel){
        this.conversationUserName.text = conversation.userName
        this.conversationLastMessage.text = conversation.lastMessage?.message

        Glide.with(itemView.context)
            .load(conversation.userProfilePicture)
            .placeholder(R.drawable.placeholder)
            .into(this.conversationUserProfilePicture)
    }
}
