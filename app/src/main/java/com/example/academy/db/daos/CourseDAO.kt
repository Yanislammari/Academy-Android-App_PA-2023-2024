package com.example.academy.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.academy.db.entities.CourseEntity
import com.example.academy.db.entities.CourseWithLesson
import com.example.academy.db.entities.LessonEntity

@Dao
interface CourseDAO {
    @Transaction
    @Query("SELECT * FROM courses")
    fun getAllCoursesWithLessons(): List<CourseWithLesson>
    @Insert
    fun addCourse(course: CourseEntity): Long
    @Insert
    fun addLessons(lessons: List<LessonEntity>)
}
