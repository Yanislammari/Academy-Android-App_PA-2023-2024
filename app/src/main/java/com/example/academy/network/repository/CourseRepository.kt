package com.example.academy.network.repository

import com.example.academy.db.AcademyDatabase
import com.example.academy.models.course.CourseModel
import com.example.academy.db.entities.CourseEntity
import com.example.academy.db.entities.LessonEntity
import com.example.academy.models.course.CourseResponses
import com.example.academy.models.lesson.LessonModel
import com.example.academy.network.services.CourseServices
import retrofit2.Call

class CourseRepository(private val courseService: CourseServices, private val academyDatabase: AcademyDatabase) {
    fun fetchCourses(token: String): Call<CourseResponses.CoursesResponse> {
        return courseService.getCourses("Bearer $token")
    }

    fun getCourseById(token: String, courseId: String?): Call<CourseResponses.CourseResponse> {
        return courseService.getCourseById("Bearer $token" ,courseId)
    }

    fun getAllCourseFromDB(): List<CourseModel> {
        return this.academyDatabase.courseDAO().getAllCoursesWithLessons().map {
            CourseModel(
                id = it.course.idCourse,
                title = it.course.title,
                imageUrl = it.course.picture,
                teacherProfilePictureUrl = it.course.teacherPicture,
                teacherName = it.course.teacherName,
                level = it.course.level,
                description = it.course.description,
                lessons = it.lessons.map { lesson ->
                    LessonModel(
                        id = null,
                        title = lesson.title,
                        video = null,
                        description = null
                    )
                },
                exercises = null
            )
        }
    }

    fun addCourseToDB(courseModel: CourseModel) {
        val courseEntity = CourseEntity(
            idCourse = courseModel.id,
            title = courseModel.title,
            picture = courseModel.imageUrl,
            level = courseModel.level,
            description = courseModel.description,
            teacherName = courseModel.teacherName ?: "",
            teacherPicture = courseModel.teacherProfilePictureUrl
        )

        Thread {
            val courseId = academyDatabase.courseDAO().addCourse(courseEntity)
            val lessons = courseModel.lessons?.map {
                LessonEntity(
                    title = it.title,
                    courseId = courseId.toInt()
                )
            }
            lessons?.let {
                academyDatabase.courseDAO().addLessons(it)
            }
        }.start()
    }
}
