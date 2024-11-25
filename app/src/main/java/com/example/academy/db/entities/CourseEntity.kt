package com.example.academy.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_of_course")
    val idCourse: String?,
    @ColumnInfo(name = "course_title")
    val title: String?,
    @ColumnInfo(name = "course_picture")
    val picture: String?,
    @ColumnInfo(name = "course_level")
    val level: String?,
    @ColumnInfo(name = "course_description")
    val description: String?,
    @ColumnInfo(name = "course_teacher_name")
    val teacherName: String?,
    @ColumnInfo(name = "course_teacher_picture")
    val teacherPicture: String?
)
