package com.example.academy.models.lesson

import android.os.Parcel
import android.os.Parcelable

data class LessonModel(val id: String?, val title: String?, val video: String?, val description: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(video)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LessonModel> {
        override fun createFromParcel(parcel: Parcel): LessonModel {
            return LessonModel(parcel)
        }

        override fun newArray(size: Int): Array<LessonModel?> {
            return arrayOfNulls(size)
        }
    }

}
