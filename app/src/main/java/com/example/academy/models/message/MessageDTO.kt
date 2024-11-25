package com.example.academy.models.message

import com.google.gson.annotations.SerializedName

data class MessageDTO(
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String,
    @SerializedName("message") val message: String,
    @SerializedName("receiver") val receiver: String,
    @SerializedName("sender") val sender: String,
    @SerializedName("timestamp") val timestamp: String
)
