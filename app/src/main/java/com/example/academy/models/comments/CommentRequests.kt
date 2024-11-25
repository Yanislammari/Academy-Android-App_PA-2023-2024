package com.example.academy.models.comments

sealed class CommentRequests {
    data class CommentRequest(
        val userId: String?,
        val courseId: String?,
        val content: String?,
    )
}
