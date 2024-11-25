package com.example.academy.views.click_listeners

import com.example.academy.models.conversation.ConversationModel

interface ConversationOnClickListener {
    fun displayConversationMessages(conversation: ConversationModel)
}
