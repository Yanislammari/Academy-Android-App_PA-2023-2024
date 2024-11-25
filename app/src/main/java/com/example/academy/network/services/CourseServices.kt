package com.example.academy.network.services

import com.example.academy.models.course.CourseResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CourseServices {
    @GET("/courses")
    fun getCourses(@Header("Authorization") token: String): Call<CourseResponses.CoursesResponse>
    @GET("/courses/{courseId}")
    fun getCourseById(@Header("Authorization") token: String, @Path("courseId") courseId: String?): Call<CourseResponses.CourseResponse>
}
