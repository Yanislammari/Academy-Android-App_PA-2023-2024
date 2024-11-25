package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.academy.models.conversation.ConversationDTO
import com.example.academy.models.conversation.ConversationModel
import com.example.academy.models.message.MessageModel
import com.example.academy.models.user.UserDTO
import com.example.academy.network.repository.ConversationRepository
import com.example.academy.network.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConversationViewModel(private val conversationRepository: ConversationRepository, private val userRepository: UserRepository): ViewModel() {
    var conversations = MutableLiveData<List<ConversationModel>?>()

    fun getConversationsById(token: String, userId: String?) {
        viewModelScope.launch {
            try {
                val conversationsAPIResponse = withContext(Dispatchers.IO) {
                    conversationRepository.getConversationById(token, userId).execute()
                }

                if(conversationsAPIResponse.isSuccessful) {
                    val responseBody = conversationsAPIResponse.body()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val updatedConversationList = responseBody?.map {
                        async {
                            val user = getUserOfTheConversation(token, it)
                            ConversationModel(
                                lastMessage = MessageModel(
                                    id = it.lastMessage.id,
                                    message = it.lastMessage.message,
                                    sender = it.lastMessage.sender,
                                    receiver = it.lastMessage.receiver,
                                    timeStamp = parseDate(it.lastMessage.timestamp, dateFormat)
                                ),
                                userName = "${user?.firstName} ${user?.lastName}",
                                userProfilePicture = user?.profilePicture,
                                user2id = user?.id,
                            )
                        }
                    }?.awaitAll()
                    conversations.postValue(updatedConversationList)
                }
                else {
                    conversations.postValue(null)
                }
            }
            catch(t: Throwable) {
                conversations.postValue(null)
            }
        }
    }

    fun parseDate(dateString: String, dateFormat: SimpleDateFormat): Date? {
        return try {
            dateFormat.parse(dateString)
        }
        catch(e: ParseException) {
            null
        }
    }

    private suspend fun getUserOfTheConversation(token: String, conversation: ConversationDTO): UserDTO? {
        return suspendCoroutine { continuation ->
            userRepository.getUserById(token, conversation.user).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if(response.isSuccessful) {
                        continuation.resume(response.body())
                    }
                    else {
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    continuation.resume(null)
                }
            })
        }
    }
}
