package com.example.academy.models.course

import com.example.academy.models.exercise.ExerciseDTO
import com.example.academy.models.lesson.LessonDTO
import com.google.gson.annotations.SerializedName

data class CourseDTO(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("description") val description: String,
    @SerializedName("exercises") val exercises: List<ExerciseDTO>,
    @SerializedName("_id") val id: String?,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("lessons") val lessons: List<LessonDTO>,
    @SerializedName("level") val level: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("teacherID") val teacherID: String,
    @SerializedName("title") val title: String
)
