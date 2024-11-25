package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academy.models.user.UserModel
import com.example.academy.models.user.UserDTO
import com.example.academy.network.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    var users = MutableLiveData<List<UserModel>?>()
    var user = MutableLiveData<UserModel?>()

    fun getUsers(token: String) {
        userRepository.getAllUsers(token).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if(response.isSuccessful) {
                    val userDTOs = response.body() ?: emptyList()
                    val userModels = userDTOs.map { userDTO ->
                        UserModel(
                            id = userDTO.id,
                            firstName = userDTO.firstName,
                            lastName = userDTO.lastName,
                            email = userDTO.email,
                            role = userDTO.role,
                            profilePicture = userDTO.profilePicture
                        )
                    }
                    users.postValue(userModels)
                }
                else {
                    users.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                t.printStackTrace()
                users.postValue(null)
            }
        })
    }

    fun getUserById(token: String, id: String?, onComplete: (UserModel?) -> Unit) {
        userRepository.getUserById(token, id).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if(response.isSuccessful) {
                    val userDTO = response.body()
                    val userModel = userDTO?.let {
                        UserModel(
                            id = it.id,
                            firstName = it.firstName,
                            lastName = it.lastName,
                            email = it.email,
                            role = it.role,
                            profilePicture = it.profilePicture
                        )
                    }
                    onComplete(userModel)
                }
                else {
                    onComplete(null)
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                t.printStackTrace()
                onComplete(null)
            }
        })
    }
}
