package com.example.academy.models.lesson

import com.google.gson.annotations.SerializedName

data class LessonDTO(
    @SerializedName("_id") val mongoId: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: String
)
