package com.example.academy.models.enrollment

sealed class EnrollmentResponses {
    data class EnrollmentsResponse(
        val enrollments: List<EnrollmentDTO>
    )
}
