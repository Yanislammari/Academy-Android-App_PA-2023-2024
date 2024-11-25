package com.example.academy.models.conversation

import com.example.academy.models.message.MessageDTO
import com.google.gson.annotations.SerializedName

data class ConversationDTO(
    @SerializedName("lastMessage") val lastMessage: MessageDTO,
    @SerializedName("user") val user: String
)
