package com.example.academy.network.services

import com.example.academy.models.auth.AuthRequests
import com.example.academy.models.user.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthServices {
    @POST("/auth/login")
    fun login(@Body loginRequest: AuthRequests.LoginRequest): Call<ResponseBody>
    @GET("/auth")
    fun decodeToken(@Header("Authorization") token: String): Call<UserDTO>
    @Multipart
    @POST("/auth/register")
    fun register(@Part("firstName") firstName: RequestBody, @Part("lastName") lastName: RequestBody, @Part("email") email: RequestBody, @Part("password") password: RequestBody, @Part profilePicture: MultipartBody.Part?): Call<ResponseBody>
}
