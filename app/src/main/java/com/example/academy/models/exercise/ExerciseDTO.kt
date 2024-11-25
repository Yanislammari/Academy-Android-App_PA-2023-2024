package com.example.academy.models.exercise

import com.google.gson.annotations.SerializedName

data class ExerciseDTO(
    @SerializedName("correctAnswers") val correctAnswers: List<String>?,
    @SerializedName("correctOption") val correctOption: String?,
    @SerializedName("description") val description: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("_id") val id: String,
    @SerializedName("options") val options: List<String>?,
    @SerializedName("textWithBlanks") val textWithBlanks: String?,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String
)
