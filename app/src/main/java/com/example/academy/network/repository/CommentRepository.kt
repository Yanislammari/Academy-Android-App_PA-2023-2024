package com.example.academy.network.repository

import com.example.academy.models.comments.CommentDTO
import com.example.academy.models.comments.CommentRequests
import com.example.academy.models.comments.CommentResponses
import com.example.academy.network.services.CommentServices
import okhttp3.ResponseBody
import retrofit2.Call

class CommentRepository(private val commentService: CommentServices) {
    fun getCommentById(token: String, id: String): Call<CommentDTO> {
        return commentService.getCommentById("Bearer $token", id)
    }

    fun getAllComments(token: String): Call<CommentResponses.CommentsResponse> {
        return commentService.getAllComments("Bearer $token")
    }

    fun postComment(token: String, comment: CommentRequests.CommentRequest): Call<CommentDTO> {
        return commentService.postComment("Bearer $token", comment)
    }

    fun deleteComment(token: String, id: String?): Call<ResponseBody>{
        return commentService.deleteComment("Bearer $token", id)
    }
}
