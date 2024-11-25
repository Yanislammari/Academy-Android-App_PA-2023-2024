package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academy.models.course.CourseModel
import com.example.academy.models.enrollment.EnrollmentDTO
import com.example.academy.models.enrollment.EnrollmentModel
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.models.lesson.LessonModel
import com.example.academy.models.progression.ProgressionModel
import com.example.academy.network.repository.CourseRepository
import com.example.academy.network.repository.EnrollmentRepository
import com.example.academy.utils.singleton.DataLayerSingleton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import okhttp3.ResponseBody

class EnrollmentViewModel(private val enrollmentRepository: EnrollmentRepository, private val courseViewModel: CourseViewModel, private val courseRepository: CourseRepository) : ViewModel() {
    val coursesOfStudent = MutableLiveData<List<CourseModel?>>()
    private val enrollmentsOfStudent = MutableLiveData<List<EnrollmentModel?>?>()
    val errorMessage = MutableLiveData<String?>()

    fun getEnrollmentsOfStudent(token: String, studentId: String?) {
        viewModelScope.launch {
            try {
                val response = enrollmentRepository.getEnrollmentsByStudentId(token, studentId).await()
                val allCoursesOfStudent = response.enrollments.map { enrollment ->
                    async {
                        val courseResponse = courseRepository.getCourseById(token, enrollment.courseId).await()
                        val teacher = courseViewModel.getTeacherOfCourse(token, courseResponse.course)
                        CourseModel(
                            id = courseResponse.course.id,
                            title = courseResponse.course.title,
                            imageUrl = courseResponse.course.imageUrl,
                            teacherName = "${teacher?.firstName} ${teacher?.lastName}",
                            teacherProfilePictureUrl = teacher?.profilePicture ?: "",
                            description = courseResponse.course.description,
                            lessons = courseResponse.course.lessons.map { lesson ->
                                LessonModel(
                                    id = lesson.mongoId,
                                    title = lesson.title,
                                    video = lesson.video,
                                    description = lesson.description
                                )
                            },
                            exercises = courseResponse.course.exercises.map { exercise ->
                                ExerciseModel(
                                    id = exercise.id,
                                    title = exercise.title,
                                    options = exercise.options,
                                    correctOption = exercise.correctOption,
                                    type = exercise.type
                                )
                            },
                            level = courseResponse.course.level
                        )
                    }
                }.awaitAll()
                coursesOfStudent.postValue(allCoursesOfStudent)
                val enrollmentsOfStudentList = response.enrollments.map {
                    EnrollmentModel(
                        id = it.id,
                        courseId = it.courseId,
                        studentId = it.studentId,
                        enrolledDate = it.enrolledDate,
                        progression = ProgressionModel(
                            lessonsFinish = it.progression.lessonsFinish,
                            exercisesSucces = it.progression.exercicesSuccess,
                            attempts = it.progression.attempts
                        ),
                        status = it.status
                    )
                }
                enrollmentsOfStudent.postValue(enrollmentsOfStudentList)
            }
            catch(t: Throwable) {
                errorMessage.postValue(t.message)
            }
        }
    }

    fun getEnrollmentOfACourse(courseId: String?): EnrollmentModel? {
        this.enrollmentsOfStudent.value?.forEach {
            if(it?.courseId == courseId) {
                return it
            }
        }
        return null
    }

    fun enrollCourse(token: String, studentId: String?, courseId: String) {
        enrollmentRepository.enrollCourse(token, studentId, courseId).enqueue(object : Callback<EnrollmentDTO> {
            override fun onResponse(call: Call<EnrollmentDTO>, response: Response<EnrollmentDTO>) {
                if(response.isSuccessful) {
                    getEnrollmentsOfStudent(token, studentId)
                }
                else {
                    errorMessage.postValue("You are already enrolled in this course!")
                }
            }

            override fun onFailure(call: Call<EnrollmentDTO>, t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        })
    }

    fun finishLesson(token: String, enrollmentId: String?, lessonId: String?) {
        enrollmentRepository.finishLesson(token, enrollmentId, lessonId).enqueue(object : Callback<EnrollmentDTO> {
            override fun onResponse(call: Call<EnrollmentDTO>, response: Response<EnrollmentDTO>) {
                if(!response.isSuccessful) {
                    errorMessage.postValue("You have already completed this lesson!")
                }
            }

            override fun onFailure(call: Call<EnrollmentDTO>, t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        })
    }

    fun correctExercises(token: String, enrollmentId: String?, exercisesId: List<String?>) {
        enrollmentRepository.correctExercises(token, enrollmentId, exercisesId).enqueue(object : Callback<EnrollmentDTO> {
            override fun onResponse(call: Call<EnrollmentDTO>, response: Response<EnrollmentDTO>) {
                if(!response.isSuccessful) {
                    errorMessage.postValue("${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EnrollmentDTO>, t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        })
    }

    fun deleteEnrollment(token: String, enrollmentId: String?) {
        enrollmentRepository.deleteEnrollment(token, enrollmentId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    getEnrollmentsOfStudent(token, DataLayerSingleton.userConnected?.id)
                }
                else {
                    errorMessage.postValue("Error deleting the enrollment")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        })
    }
}
