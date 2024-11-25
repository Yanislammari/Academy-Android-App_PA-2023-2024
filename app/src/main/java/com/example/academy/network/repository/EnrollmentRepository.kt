package com.example.academy.network.repository

import com.example.academy.models.enrollment.EnrollmentResponses
import com.example.academy.models.enrollment.EnrollmentDTO
import com.example.academy.models.exercise.ExerciseRequests
import com.example.academy.network.services.EnrollmentServices
import okhttp3.ResponseBody
import retrofit2.Call

class EnrollmentRepository(private val enrollmentServices: EnrollmentServices) {
    fun getEnrollmentsByStudentId(token: String, studentId: String?): Call<EnrollmentResponses.EnrollmentsResponse> {
        return enrollmentServices.getEnrollmentsByStudentId("Bearer $token" ,studentId)
    }

    fun enrollCourse(token: String, studentId: String?, courseId: String?): Call<EnrollmentDTO> {
        return enrollmentServices.enrollCourse("Bearer $token", studentId, courseId)
    }

    fun finishLesson(token: String, enrollmentId: String?, lessonId: String?): Call<EnrollmentDTO> {
        return enrollmentServices.finishLesson("Bearer $token" ,enrollmentId, lessonId)
    }

    fun correctExercises(token: String, enrollmentId: String?, exercisesId: List<String?>): Call<EnrollmentDTO> {
        val requestBody = ExerciseRequests.ExercisesIdRequest(exercisesId)
        return enrollmentServices.correctExercises("Bearer $token", enrollmentId, requestBody)
    }

    fun deleteEnrollment(token: String, enrollmentId: String?): Call<ResponseBody> {
        return enrollmentServices.deleteEnrollment("Bearer $token", enrollmentId)
    }
}
