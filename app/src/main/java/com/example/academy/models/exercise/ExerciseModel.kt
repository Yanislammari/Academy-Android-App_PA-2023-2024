package com.example.academy.models.exercise

import android.os.Parcel
import android.os.Parcelable

data class ExerciseModel(val id: String?, val title: String?, val options: List<String>?, val correctOption: String?, val type: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeStringList(options)
        parcel.writeString(correctOption)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExerciseModel> {
        override fun createFromParcel(parcel: Parcel): ExerciseModel {
            return ExerciseModel(parcel)
        }

        override fun newArray(size: Int): Array<ExerciseModel?> {
            return arrayOfNulls(size)
        }
    }
}
