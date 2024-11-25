package com.example.academy.models.comments

sealed class CommentResponses {
    data class CommentsResponse(
        val comments: List<CommentDTO>
    )
}
