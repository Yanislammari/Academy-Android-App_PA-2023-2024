package com.example.academy.models.course

import android.os.Parcel
import android.os.Parcelable
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.models.lesson.LessonModel

data class CourseModel(val id: String?, val title: String?, val imageUrl: String?, val teacherProfilePictureUrl: String?, val teacherName: String?, val description: String?, val lessons: List<LessonModel>?, val exercises: List<ExerciseModel>?, val level: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(LessonModel) ?: emptyList(),
        parcel.createTypedArrayList(ExerciseModel) ?: emptyList(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(teacherProfilePictureUrl)
        parcel.writeString(teacherName)
        parcel.writeString(description)
        parcel.writeTypedList(lessons)
        parcel.writeTypedList(exercises)
        parcel.writeString(level)
    }

    companion object CREATOR : Parcelable.Creator<CourseModel> {
        override fun createFromParcel(parcel: Parcel): CourseModel {
            return CourseModel(parcel)
        }

        override fun newArray(size: Int): Array<CourseModel?> {
            return arrayOf()
        }
    }
}
