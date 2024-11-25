package com.example.academy.network.services

import com.example.academy.models.enrollment.EnrollmentResponses
import com.example.academy.models.enrollment.EnrollmentDTO
import com.example.academy.models.exercise.ExerciseRequests
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EnrollmentServices {
    @GET("/enrollments/{studentId}")
    fun getEnrollmentsByStudentId(@Header("Authorization") token: String, @Path("studentId") studentId: String?): Call<EnrollmentResponses.EnrollmentsResponse>
    @POST("/enrollments/enrollCourse/{studentId}/{courseId}")
    fun enrollCourse(@Header("Authorization") token: String, @Path("studentId") studentId: String?, @Path("courseId") courseId: String?): Call<EnrollmentDTO>
    @POST("/enrollments/addLessonFinish/{enrollmentId}/{lessonId}")
    fun finishLesson(@Header("Authorization") token: String, @Path("enrollmentId") enrollmentId: String?, @Path("lessonId") lessonId: String?): Call<EnrollmentDTO>
    @POST("/enrollments/addExercisesSuccess/{enrollmentId}")
    fun correctExercises(@Header("Authorization") token: String, @Path("enrollmentId") enrollmentId: String?, @Body exercisesIdRequest: ExerciseRequests.ExercisesIdRequest): Call<EnrollmentDTO>
    @DELETE("/enrollments/{enrollmentId}")
    fun deleteEnrollment(@Header("Authorization") token: String, @Path("enrollmentId") enrollmentId: String?): Call<ResponseBody>
}
