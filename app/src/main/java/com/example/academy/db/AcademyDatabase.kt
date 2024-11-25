package com.example.academy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.academy.db.daos.CourseDAO
import com.example.academy.db.entities.CourseEntity
import com.example.academy.db.entities.LessonEntity

@Database(
    entities = [CourseEntity::class, LessonEntity::class],
    version = 1
)
abstract class AcademyDatabase: RoomDatabase() {
    abstract fun courseDAO(): CourseDAO
}
