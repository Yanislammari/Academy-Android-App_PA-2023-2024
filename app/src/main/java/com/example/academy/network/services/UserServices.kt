package com.example.academy.network.services

import com.example.academy.models.user.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserServices {
    @GET("/users/{id}")
    fun getUserById(@Header("Authorization") token: String, @Path("id") id: String?): Call<UserDTO>
    @GET("/users")
    fun getAllUsers(@Header("Authorization") token: String): Call<List<UserDTO>>
}
