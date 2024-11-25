package com.example.academy.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.academy.models.auth.AuthRequests
import com.example.academy.models.user.UserDTO
import com.example.academy.models.user.UserModel
import com.example.academy.network.repository.AuthRepository
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AuthViewModel(private val authRepository: AuthRepository, private val context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    var token = MutableLiveData<String?>()
    var userConnected = MutableLiveData<UserModel?>()
    val errorMessage = MutableLiveData<String?>()

    init {
        loadToken()
    }

    private fun loadToken() {
        val savedToken = this.sharedPreferences.getString("auth_token", null)
        if(savedToken != null) {
            token.value = savedToken
            getUserConnected()
        }
    }

    fun login(email: String, password: String) {
        val loginRequest = AuthRequests.LoginRequest(email, password)
        authRepository.login(loginRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    try {
                        val jsonResponse = response.body()?.string()
                        val jsonObject = JSONObject(jsonResponse ?: "")
                        val tokenResponse = jsonObject.getString("token")
                        sharedPreferences.edit().putString("auth_token", tokenResponse).apply()
                        token.postValue(tokenResponse)

                        token.observeForever(object : Observer<String?> {
                            override fun onChanged(updatedToken: String?) {
                                if(updatedToken != null) {
                                    token.removeObserver(this)
                                    getUserConnected()
                                }
                            }
                        })
                    }
                    catch(e: JSONException) {
                        errorMessage.postValue("Error processing JSON response: ${e.message}")
                    }
                }
                else {
                    errorMessage.postValue("Incorrect email or password!")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorMessage.postValue("Server error: ${t.message}")
            }
        })
    }

    fun getUserConnected() {
        val currentToken = token.value
        if(currentToken != null) {
            authRepository.decodeToken(currentToken).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if(response.isSuccessful) {
                        val userJson = response.body()
                        val userConnectedResponse = userJson?.let {
                            UserModel(
                                id = it.id,
                                firstName = it.firstName,
                                lastName = it.lastName,
                                email = it.email,
                                role = it.role,
                                profilePicture = it.profilePicture
                            )
                        }
                        userConnected.postValue(userConnectedResponse)
                    }
                    else {
                        userConnected.postValue(null)
                        errorMessage.postValue("Error while retrieving the user! : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    errorMessage.postValue("Server error! : ${t.message}")
                }
            })
        }
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
        token.postValue(null)
        userConnected.postValue(null)
    }

    fun register(firstName: String, lastName: String, email: String, password: String, profilePicture: File?, callback: (Boolean) -> Unit) {
        val registerRequest = AuthRequests.RegisterRequest(firstName, lastName, email, password, profilePicture)
        authRepository.register(registerRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    try {
                        val jsonResponse = response.body()?.string()
                        val jsonObject = JSONObject(jsonResponse ?: "")

                        val userConnectedResponse = UserModel(
                            id = jsonObject.getString("_id"),
                            firstName = jsonObject.getString("firstName"),
                            lastName = jsonObject.getString("lastName"),
                            email = jsonObject.getString("email"),
                            role = jsonObject.getString("role"),
                            profilePicture = jsonObject.getString("profilePicture")
                        )

                        sharedPreferences.edit()
                            .putString("user_id", userConnectedResponse.id)
                            .putString("user_firstName", userConnectedResponse.firstName)
                            .putString("user_lastName", userConnectedResponse.lastName)
                            .putString("user_email", userConnectedResponse.email)
                            .putString("user_role", userConnectedResponse.role)
                            .putString("user_profilePicture", userConnectedResponse.profilePicture)
                            .apply()

                        callback(true)
                    }
                    catch(e: JSONException) {
                        errorMessage.postValue("Error processing JSON response: ${e.message}")
                        callback(false)
                    }
                }
                else {
                    errorMessage.postValue("Registration failed! : ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorMessage.postValue("Server error! : ${t.message}")
                callback(false)
            }
        })
    }
}
