package com.example.academy.views.click_listeners

import com.example.academy.models.course.CourseModel

interface CourseOnClickListener {
    fun displayCourseDetail(course: CourseModel?)
    fun verifyCourseExists(courseId: String?)
}
