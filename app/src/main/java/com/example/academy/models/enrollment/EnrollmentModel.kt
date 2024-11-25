package com.example.academy.models.enrollment

import com.example.academy.models.progression.ProgressionModel

data class EnrollmentModel(
    val id: String,
    val courseId: String,
    val studentId: String,
    val enrolledDate: String,
    val progression: ProgressionModel,
    val status: String
)
