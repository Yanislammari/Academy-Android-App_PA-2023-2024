package com.example.academy.network.repository

import com.example.academy.models.user.UserDTO
import com.example.academy.network.services.UserServices
import retrofit2.Call

class UserRepository(private val userService: UserServices) {
    fun getUserById(token: String, id: String?): Call<UserDTO> {
        return userService.getUserById("Bearer $token", id)
    }

    fun getAllUsers(token: String): Call<List<UserDTO>> {
        return userService.getAllUsers("Bearer $token")
    }
}
