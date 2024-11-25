package com.example.academy.network.services

import com.example.academy.models.comments.CommentDTO
import com.example.academy.models.comments.CommentRequests
import com.example.academy.models.comments.CommentResponses
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header

interface CommentServices {
    @GET("/comments/{id}")
    fun getCommentById(@Header("Authorization") token: String, @Path("id") id: String): Call<CommentDTO>
    @GET("/comments")
    fun getAllComments(@Header("Authorization") token: String): Call<CommentResponses.CommentsResponse>
    @POST("/comments")
    fun postComment(@Header("Authorization") token: String, @Body commentRequest: CommentRequests.CommentRequest): Call<CommentDTO>
    @DELETE("/comments/{id}")
    fun deleteComment(@Header("Authorization") token: String, @Path("id") id: String?): Call<ResponseBody>
}
