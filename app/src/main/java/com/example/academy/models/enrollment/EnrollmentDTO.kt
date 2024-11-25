package com.example.academy.models.enrollment

import com.example.academy.models.progression.ProgressionDTO
import com.google.gson.annotations.SerializedName

data class EnrollmentDTO(
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("enrolledDate") val enrolledDate: String,
    @SerializedName("progression") val progression: ProgressionDTO,
    @SerializedName("status") val status: String,
    @SerializedName("studentId") val studentId: String
)
