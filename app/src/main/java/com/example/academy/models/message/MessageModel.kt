package com.example.academy.models.message

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class MessageModel(val id: String, val message: String, val sender: String, val receiver: String, val timeStamp: Date?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(message)
        parcel.writeString(sender)
        parcel.writeString(receiver)
        timeStamp?.time?.let {
            parcel.writeLong(it)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageModel> {
        override fun createFromParcel(parcel: Parcel): MessageModel {
            return MessageModel(parcel)
        }

        override fun newArray(size: Int): Array<MessageModel?> {
            return arrayOfNulls(size)
        }
    }
}
