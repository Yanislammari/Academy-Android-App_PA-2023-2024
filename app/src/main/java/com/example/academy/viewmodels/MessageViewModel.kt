package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.academy.models.message.MessageRequests
import com.example.academy.models.message.MessageDTO
import com.example.academy.models.message.MessageModel
import com.example.academy.network.repository.MessageRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageViewModel(private val messageRepository: MessageRepository) {
    var messages = MutableLiveData<List<MessageModel>?>()

    fun getMessages(token: String, user1: String?, user2: String) {
        messageRepository.getMessages(token, user1, user2).enqueue(object : Callback<List<MessageDTO>> {
            override fun onResponse(call: Call<List<MessageDTO>>, response: Response<List<MessageDTO>>) {
                if(response.isSuccessful) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val messagesResponse = response.body()?.map {
                        MessageModel(
                            id = it.id,
                            message = it.message,
                            receiver = it.receiver,
                            sender = it.sender,
                            timeStamp = parseDate(it.timestamp, dateFormat)
                        )
                    }
                    messages.postValue(messagesResponse)
                }
                else {
                    messages.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<MessageDTO>>, t: Throwable) {
                messages.postValue(null)
            }
        })
    }

    fun parseDate(dateString: String, dateFormat: SimpleDateFormat): Date? {
        return try {
            dateFormat.parse(dateString)
        }
        catch(e: ParseException) {
            null
        }
    }

    fun sendMessage(token: String, sender: String?, receiver: String, message: String) {
        val messageRequest = MessageRequests.MessageRequest(sender, receiver, message)
        messageRepository.sendMessage(token, messageRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    getMessages(token, sender, receiver)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
