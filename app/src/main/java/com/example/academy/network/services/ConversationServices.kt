package com.example.academy.network.services

import com.example.academy.models.conversation.ConversationDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConversationServices {
    @GET("/messages/conversations/{userId}")
    fun getConversationsById(@Header("Authorization") token: String, @Path("userId") userId: String?): Call<List<ConversationDTO>>
}
