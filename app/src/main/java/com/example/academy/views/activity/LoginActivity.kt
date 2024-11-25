package com.example.academy.views.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.academy.R
import com.example.academy.utils.singleton.DataLayerSingleton

class LoginActivity : AppCompatActivity() {
    private lateinit var loginEmailEditText: EditText
    private lateinit var loginPasswordEditText: EditText
    private lateinit var loginSubmitButton: Button
    private lateinit var loginNotSignUpTextView: TextView
    private var authViewModel = DataLayerSingleton.getAuthViewModel()

    companion object {
        const val USER_MODEL_EXTRA = "USER_MODEL_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        loginEmailEditText = findViewById(R.id.login_email_edit_text)
        loginPasswordEditText = findViewById(R.id.login_password_edit_text)
        loginSubmitButton = findViewById(R.id.login_submit_button)
        loginNotSignUpTextView = findViewById(R.id.login_not_sign_up_text_view)

        loginSubmitButton.setOnClickListener {
            val email = loginEmailEditText.text.toString()
            val password = loginPasswordEditText.text.toString()
            this.authViewModel.login(email, password)
        }

        loginNotSignUpTextView.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.token.observe(this) { token ->
            if(token != null) {
                authViewModel.getUserConnected()
            }
        }

        authViewModel.userConnected.observe(this) { user ->
            if(user != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "HomeFragment")
                intent.putExtra(USER_MODEL_EXTRA, user)
                startActivity(intent)
                finish()
            }
        }

        authViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
