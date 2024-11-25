package com.example.academy.models.course

sealed class CourseResponses {
    data class CourseResponse(
        val course: CourseDTO
    ) : CourseResponses()

    data class CoursesResponse(
        val courses: List<CourseDTO>
    ) : CourseResponses()
}
