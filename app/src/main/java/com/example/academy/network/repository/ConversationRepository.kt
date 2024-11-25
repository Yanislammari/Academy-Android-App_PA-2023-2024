package com.example.academy.network.repository

import com.example.academy.models.conversation.ConversationDTO
import com.example.academy.network.services.ConversationServices
import retrofit2.Call

class ConversationRepository(private val conversationServices: ConversationServices) {
    fun getConversationById(token: String, userId: String?): Call<List<ConversationDTO>> {
        return conversationServices.getConversationsById("Bearer $token", userId)
    }
}
