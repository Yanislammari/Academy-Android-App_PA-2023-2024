package com.example.academy.network.repository

import com.example.academy.models.message.MessageRequests
import com.example.academy.models.message.MessageDTO
import com.example.academy.network.services.MessageServices
import okhttp3.ResponseBody
import retrofit2.Call

class MessageRepository(private val messageServices: MessageServices) {
    fun getMessages(token: String, user1: String?, user2: String): Call<List<MessageDTO>> {
        return messageServices.getMessages("Bearer $token", user1, user2)
    }

    fun sendMessage(token: String, messageRequest: MessageRequests.MessageRequest): Call<ResponseBody>{
        return messageServices.sendMessage("Bearer $token", messageRequest)
    }
}
