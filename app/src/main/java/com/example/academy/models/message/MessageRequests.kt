package com.example.academy.models.message

sealed class MessageRequests {
    data class MessageRequest(
        val sender: String?,
        val receiver: String,
        val message: String
    )
}
