package com.example.academy.models.comments

import com.google.gson.annotations.SerializedName

data class CommentDTO(
    @SerializedName("_id") val id: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("courseId") val courseId: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("createdAt") val timestamp: String?
)
