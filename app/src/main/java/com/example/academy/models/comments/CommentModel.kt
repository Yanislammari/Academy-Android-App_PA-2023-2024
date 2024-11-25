package com.example.academy.models.comments

import android.os.Parcel
import android.os.Parcelable

data class CommentModel(val id: String?, val userId: String?, val courseId: String?, val content: String?, val timestamp: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(courseId)
        parcel.writeString(content)
        parcel.writeValue(timestamp)
    }

    companion object CREATOR : Parcelable.Creator<CommentModel> {
        override fun createFromParcel(parcel: Parcel): CommentModel {
            return CommentModel(parcel)
        }

        override fun newArray(size: Int): Array<CommentModel?> {
            return arrayOfNulls(size)
        }
    }
}
