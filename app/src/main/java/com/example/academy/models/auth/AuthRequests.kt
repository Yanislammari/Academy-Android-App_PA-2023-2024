package com.example.academy.models.auth

import java.io.File

sealed class AuthRequests {
    data class LoginRequest(
        val email: String,
        val password: String
    ) : AuthRequests()

    data class RegisterRequest(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val profilePicture: File?
    ) : AuthRequests()
}
