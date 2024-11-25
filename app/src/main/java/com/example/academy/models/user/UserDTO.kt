package com.example.academy.models.user

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("_id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("profilePicture") val profilePicture: String,
    @SerializedName("role") val role: String
)
