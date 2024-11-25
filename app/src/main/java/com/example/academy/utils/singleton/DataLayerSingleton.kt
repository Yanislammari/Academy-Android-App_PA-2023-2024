package com.example.academy.utils.singleton

import android.content.Context
import androidx.room.Room
import com.example.academy.db.AcademyDatabase
import com.example.academy.models.user.UserModel
import com.example.academy.network.repository.AuthRepository
import com.example.academy.network.repository.CommentRepository
import com.example.academy.network.repository.ConversationRepository
import com.example.academy.network.services.AuthServices
import com.example.academy.network.services.CourseServices
import com.example.academy.network.repository.CourseRepository
import com.example.academy.network.repository.EnrollmentRepository
import com.example.academy.network.repository.MessageRepository
import com.example.academy.network.services.EnrollmentServices
import com.example.academy.network.repository.UserRepository
import com.example.academy.network.services.CommentServices
import com.example.academy.network.services.ConversationServices
import com.example.academy.network.services.MessageServices
import com.example.academy.network.services.UserServices
import com.example.academy.viewmodels.AuthViewModel
import com.example.academy.viewmodels.CommentViewModel
import com.example.academy.viewmodels.ConversationViewModel
import com.example.academy.viewmodels.CourseViewModel
import com.example.academy.viewmodels.EnrollmentViewModel
import com.example.academy.viewmodels.MessageViewModel
import com.example.academy.viewmodels.UserViewModel
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.github.cdimascio.dotenv.dotenv

object DataLayerSingleton {
    private lateinit var retrofitClient: Retrofit
    private lateinit var courseService: CourseServices
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var enrollmentViewModel: EnrollmentViewModel
    private lateinit var conversationViewModel: ConversationViewModel
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var authService: AuthServices
    private lateinit var enrollmentService: EnrollmentServices
    private lateinit var conversationService: ConversationServices
    private lateinit var messageService: MessageServices
    private lateinit var userService: UserServices
    private lateinit var commentServices: CommentServices
    private lateinit var academyDB: AcademyDatabase

    var userConnected: UserModel? = null

    init {
        createRetrofitClient()
        createCourseService()
        createUserService()
        createAuthService()
        createEnrollmentService()
        createConversationService()
        createMessageService()
        createCommentService()
    }

    fun initialize(context: Context) {
        getRoomDb(context)
        initViewModel(context)
    }

    fun getCourseViewModel() = courseViewModel
    fun getAuthViewModel() = authViewModel
    fun getEnrollmentViewModel() = enrollmentViewModel
    fun getConversationViewModel() = conversationViewModel
    fun getMessageViewModel() = messageViewModel
    fun getUserViewModel() = userViewModel
    fun getCommentViewModel() = commentViewModel

    private fun getRoomDb(context: Context) {
        academyDB = Room.databaseBuilder(
            context.applicationContext,
            AcademyDatabase::class.java,
            "academy-db"
        ).build()
    }

    private fun createRetrofitClient() {
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val gsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        retrofitClient = Retrofit.Builder()
            .baseUrl(dotenv["BASE_URL"])
            .addConverterFactory(gsonConverter)
            .build()
    }

    private fun createCourseService() {
        courseService = retrofitClient.create(CourseServices::class.java)
    }

    private fun createUserService() {
        userService = retrofitClient.create(UserServices::class.java)
    }

    private fun createAuthService() {
        authService = retrofitClient.create(AuthServices::class.java)
    }

    private fun createEnrollmentService() {
        enrollmentService = retrofitClient.create(EnrollmentServices::class.java)
    }

    private fun createConversationService() {
        conversationService = retrofitClient.create(ConversationServices::class.java)
    }

    private fun createMessageService() {
        messageService = retrofitClient.create(MessageServices::class.java)
    }

    private fun createCommentService() {
        commentServices = retrofitClient.create(CommentServices::class.java)
    }

    private fun initViewModel(context: Context) {
        courseViewModel = CourseViewModel(
            CourseRepository(courseService, academyDB),
            UserRepository(userService)
        )

        authViewModel = AuthViewModel(
            AuthRepository(authService),
            context
        )

        enrollmentViewModel = EnrollmentViewModel(
            EnrollmentRepository(enrollmentService),
            courseViewModel,
            CourseRepository(courseService, academyDB)
        )

        conversationViewModel = ConversationViewModel(
            ConversationRepository(conversationService),
            UserRepository(userService)
        )

        messageViewModel = MessageViewModel(
            MessageRepository(messageService)
        )

        userViewModel = UserViewModel(
            UserRepository(userService)
        )

        commentViewModel = CommentViewModel(
            CommentRepository(commentServices)
        )
    }
}
