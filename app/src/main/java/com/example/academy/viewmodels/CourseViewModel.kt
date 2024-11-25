package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academy.models.course.CourseDTO
import com.example.academy.models.course.CourseModel
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.models.lesson.LessonModel
import com.example.academy.models.user.UserDTO
import com.example.academy.network.repository.CourseRepository
import com.example.academy.network.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.awaitAll

class CourseViewModel(private val courseRepository: CourseRepository, private val userRepository: UserRepository): ViewModel() {
    var courses: MutableLiveData<List<CourseModel>> = MutableLiveData()
    val errorMessage = MutableLiveData<String?>()
    var localCourses: MutableLiveData<List<CourseModel>> = MutableLiveData()
    var courseDetail: MutableLiveData<CourseModel?> = MutableLiveData()

    fun fetchCoursesFromRepository(token: String) {
        viewModelScope.launch {
            try {
                val coursesAPIResponse = withContext(Dispatchers.IO) {
                    courseRepository.fetchCourses(token).execute()
                }

                if(coursesAPIResponse.isSuccessful) {
                    val responseBody = coursesAPIResponse.body()
                    responseBody?.let { coursesResponse ->
                        val updatedCourseList = coursesResponse.courses.map { course ->
                            async {
                                val teacher = getTeacherOfCourse(token, course)
                                CourseModel(
                                    id = course.id,
                                    title = course.title,
                                    imageUrl = course.imageUrl,
                                    teacherName = "${teacher?.firstName} ${teacher?.lastName}",
                                    teacherProfilePictureUrl = teacher?.profilePicture ?: "",
                                    description = course.description,
                                    lessons = course.lessons.map { lesson ->
                                        LessonModel(
                                            id = lesson.mongoId,
                                            title = lesson.title,
                                            video = lesson.video,
                                            description = lesson.description
                                        )
                                    },
                                    exercises = course.exercises.map { exercise ->
                                        ExerciseModel(
                                            id = exercise.id,
                                            title = exercise.title,
                                            options = exercise.options,
                                            correctOption = exercise.correctOption,
                                            type = exercise.type
                                        )
                                    },
                                    level = course.level
                                )
                            }
                        }.awaitAll()
                        courses.postValue(updatedCourseList)
                    }
                }
                else {
                    errorMessage.postValue("Error fetching courses: ${coursesAPIResponse.message()}")
                }
            }
            catch(t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        }
    }

    suspend fun getTeacherOfCourse(token: String, course: CourseDTO): UserDTO? {
        return suspendCoroutine { continuation ->
            userRepository.getUserById(token, course.teacherID).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if(response.isSuccessful) {
                        continuation.resume(response.body())
                    }
                    else {
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    continuation.resume(null)
                }
            })
        }
    }

    fun getAllCourseFromDB() {
        viewModelScope.launch {
            val localCoursesList = withContext(Dispatchers.IO) {
                courseRepository.getAllCourseFromDB()
            }
            localCourses.postValue(localCoursesList)
        }
    }

    fun addCourseToDB(courseModel: CourseModel) {
        viewModelScope.launch {
            try {
                courseRepository.addCourseToDB(courseModel)
            }
            catch(t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    fun getCourseById(token: String, courseId: String?) {
        viewModelScope.launch {
            try {
                val courseAPIResponse = withContext(Dispatchers.IO) {
                    courseRepository.getCourseById(token, courseId).execute()
                }

                if(courseAPIResponse.isSuccessful) {
                    val responseBody = courseAPIResponse.body()
                    responseBody?.let { courseResponse ->
                        val courseDTO = courseResponse.course
                        val teacher = getTeacherOfCourse(token, courseDTO)
                        val courseModel = CourseModel(
                            id = courseDTO.id,
                            title = courseDTO.title,
                            imageUrl = courseDTO.imageUrl,
                            teacherName = "${teacher?.firstName} ${teacher?.lastName}",
                            teacherProfilePictureUrl = teacher?.profilePicture ?: "",
                            description = courseDTO.description,
                            lessons = courseDTO.lessons.map { lesson ->
                                LessonModel(
                                    id = lesson.mongoId,
                                    title = lesson.title,
                                    video = lesson.video,
                                    description = lesson.description
                                )
                            },
                            exercises = courseDTO.exercises.map { exercise ->
                                ExerciseModel(
                                    id = exercise.id,
                                    title = exercise.title,
                                    options = exercise.options,
                                    correctOption = exercise.correctOption,
                                    type = exercise.type
                                )
                            },
                            level = courseDTO.level
                        )
                        courseDetail.postValue(courseModel)
                    }
                }
                else {
                    errorMessage.postValue("Error fetching course: ${courseAPIResponse.message()}")
                }
            }
            catch(t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        }
    }
}
