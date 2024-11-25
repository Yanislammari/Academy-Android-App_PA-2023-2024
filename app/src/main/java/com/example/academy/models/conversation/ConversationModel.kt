package com.example.academy.models.conversation

import android.os.Parcel
import android.os.Parcelable
import com.example.academy.models.message.MessageModel

data class ConversationModel(val lastMessage: MessageModel?, val userName: String?, val userProfilePicture: String?, val user2id: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MessageModel::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(lastMessage, flags)
        parcel.writeString(userName)
        parcel.writeString(userProfilePicture)
        parcel.writeString(user2id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConversationModel> {
        override fun createFromParcel(parcel: Parcel): ConversationModel {
            return ConversationModel(parcel)
        }

        override fun newArray(size: Int): Array<ConversationModel?> {
            return arrayOfNulls(size)
        }
    }
}
