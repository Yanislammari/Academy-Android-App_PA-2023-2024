package com.example.academy.views.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.academy.R
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.AuthViewModel
import com.example.academy.models.user.UserModel

class MainStartActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataLayerSingleton.initialize(this)
        authViewModel = DataLayerSingleton.getAuthViewModel()
        observeViewModel()
        checkStoredUser()
    }

    private fun observeViewModel() {
        authViewModel.token.observe(this, Observer { token ->
            if(token != null) {
                authViewModel.getUserConnected()
            }
        })

        authViewModel.userConnected.observe(this, Observer { user ->
            if(user != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "HomeFragment")
                intent.putExtra(LoginActivity.USER_MODEL_EXTRA, user)
                startActivity(intent)
                finish()
            }
        })

        authViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkStoredUser() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)

        if(authToken != null) {
            authViewModel.getUserConnected()

            authViewModel.userConnected.observe(this, Observer { user ->
                if(user != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "HomeFragment")
                    intent.putExtra(MainActivity.USER_MODEL_EXTRA, user)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "User not found or invalid token", Toast.LENGTH_LONG).show()
                }
            })

        }
        else {
            setContentView(R.layout.activity_main_start)
            setupViews()
            displayLoginView()
            displayRegisterView()
        }
    }

    private fun setupViews() {
        this.loginButton = findViewById(R.id.main_button_login)
        this.registerButton = findViewById(R.id.main_button_register)
    }

    private fun displayLoginView() {
        this.loginButton.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun displayRegisterView() {
        this.registerButton.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}
