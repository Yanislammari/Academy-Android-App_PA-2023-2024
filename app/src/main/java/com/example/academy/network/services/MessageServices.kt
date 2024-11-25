package com.example.academy.network.services

import com.example.academy.models.message.MessageRequests
import com.example.academy.models.message.MessageDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageServices {
    @GET("/messages/fetch")
    fun getMessages(@Header("Authorization") token: String, @Query("user1") user1: String?, @Query("user2") user2: String): Call<List<MessageDTO>>
    @POST("/messages/send")
    fun sendMessage(@Header("Authorization") token: String, @Body messageRequest: MessageRequests.MessageRequest): Call<ResponseBody>
}
