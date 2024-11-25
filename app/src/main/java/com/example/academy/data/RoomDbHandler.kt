package com.example.academy.data

import android.content.Context
import androidx.room.Room
import com.example.academy.db.AcademyDatabase

object RoomDbHandler {
    fun getRoomDb(context: Context): AcademyDatabase {
        return Room.databaseBuilder(
            context,
            AcademyDatabase::class.java,
            "academy-db"
        ).build()
    }
}
