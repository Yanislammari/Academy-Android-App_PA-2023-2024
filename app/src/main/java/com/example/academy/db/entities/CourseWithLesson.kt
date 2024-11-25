package com.example.academy.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithLesson(
    @Embedded val course: CourseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val lessons: List<LessonEntity>
)
