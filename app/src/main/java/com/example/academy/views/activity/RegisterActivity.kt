package com.example.academy.views.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.academy.R
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.utils.top_level_functions.drawableToFile
import com.example.academy.utils.top_level_functions.getFileName
import com.example.academy.utils.top_level_functions.isPasswordValid
import com.example.academy.viewmodels.AuthViewModel
import java.io.File

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerFirstNameEditText: EditText
    private lateinit var registerLastNameEditText: EditText
    private lateinit var registerEmailEditText: EditText
    private lateinit var registerPasswordEditText: EditText
    private lateinit var registerProfilePictureImageView: ImageView
    private lateinit var registerSubmitButton: Button
    private lateinit var registerAlreadySignUpTextView: TextView
    private val authViewModel: AuthViewModel = DataLayerSingleton.getAuthViewModel()

    companion object {
        const val USER_MODEL_EXTRA = "USER_MODEL_EXTRA"
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupView()
        observeViewModel()
    }

    private fun setupView() {
        registerFirstNameEditText = findViewById(R.id.register_firstname_edit_text)
        registerLastNameEditText = findViewById(R.id.register_lastname_edit_text)
        registerEmailEditText = findViewById(R.id.register_email_edit_text)
        registerPasswordEditText = findViewById(R.id.register_password_edit_text)
        registerProfilePictureImageView = findViewById(R.id.register_profile_picture_image_view)
        registerSubmitButton = findViewById(R.id.register_submit_button)
        registerAlreadySignUpTextView = findViewById(R.id.register_already_sing_up_text_view)

        registerProfilePictureImageView.setOnClickListener {
            chooseImage()
        }

        registerSubmitButton.setOnClickListener {
            val firstName: String = this.registerFirstNameEditText.text.toString()
            val lastName: String = this.registerLastNameEditText.text.toString()
            val email: String = this.registerEmailEditText.text.toString()
            val password: String = this.registerPasswordEditText.text.toString()

            if(!isPasswordValid(password)) {
                Toast.makeText(
                    this,
                    "The password must contain at least 8 characters, including at least one digit, one special character, and one uppercase letter.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val profilePictureDrawable: Drawable = this.registerProfilePictureImageView.drawable
            val profilePicture: File? = drawableToFile(
                this,
                profilePictureDrawable,
                getFileName(selectedImageUri, contentResolver) ?: "profile_picture.png"
            )

            authViewModel.register(firstName, lastName, email, password, profilePicture) { isSuccess ->
                if(isSuccess) {
                    authViewModel.login(email, password)
                }
                else {
                    Toast.makeText(this, "Registration failed, please try again.", Toast.LENGTH_LONG).show()
                }
            }
        }

        this.registerAlreadySignUpTextView.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.userConnected.observe(this) { user ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "HomeFragment")
            intent.putExtra(USER_MODEL_EXTRA, user)
            startActivity(intent)
            finish()
        }

        authViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private var selectedImageUri: Uri = "".toUri()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            selectedImageUri.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                registerProfilePictureImageView.setImageDrawable(BitmapDrawable(resources, bitmap))
            }
        }
    }
}
