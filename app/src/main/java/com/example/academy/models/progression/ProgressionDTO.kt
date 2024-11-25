package com.example.academy.models.progression

import com.google.gson.annotations.SerializedName

data class ProgressionDTO(
    @SerializedName("_id") val id: String,
    @SerializedName("exercicesSuccess") val exercicesSuccess: List<String>,
    @SerializedName("lessonsFinish") val lessonsFinish: List<String>,
    @SerializedName("attempts") val attempts: Int
)
