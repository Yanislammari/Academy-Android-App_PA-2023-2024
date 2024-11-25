package com.example.academy.network.repository

import com.example.academy.models.auth.AuthRequests
import com.example.academy.models.user.UserDTO
import com.example.academy.network.services.AuthServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call

class AuthRepository(private val authServices: AuthServices) {
    fun login(loginRequest: AuthRequests.LoginRequest): Call<ResponseBody> {
        return authServices.login(loginRequest)
    }

    fun decodeToken(token: String): Call<UserDTO> {
        return authServices.decodeToken("Bearer $token")
    }

    fun register(registerRequest: AuthRequests.RegisterRequest): Call<ResponseBody> {
        registerRequest.let {
            val firstNameBody = it.firstName.toRequestBody(MultipartBody.FORM)
            val lastNameBody = it.lastName.toRequestBody(MultipartBody.FORM)
            val emailBody = it.email.toRequestBody(MultipartBody.FORM)
            val passwordBody = it.password.toRequestBody(MultipartBody.FORM)
            val profilePicturePart = it.profilePicture?.let { ppPart ->
                val requestFile = ppPart.asRequestBody("image/png".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("profilePicture", ppPart.name, requestFile)
            }
            return authServices.register(firstNameBody, lastNameBody, emailBody, passwordBody, profilePicturePart)
        }
    }
}
