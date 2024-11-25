package com.example.academy.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.comments.CommentModel
import com.example.academy.models.course.CourseModel
import com.example.academy.models.lesson.LessonModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.click_listeners.LessonOnClickListener
import com.example.academy.views.fragment.HomeFragment.Companion.COURSE_MODEL_EXTRA
import com.example.academy.views.recycler_view.comments_recycler_view.CommentsListAdapter
import com.example.academy.views.recycler_view.course_details_lessons_recycler_view.CourseDetailsLessonsListAdapter
import com.google.android.material.button.MaterialButton

class CourseDetailsActivity : AppCompatActivity(), LessonOnClickListener {
    private lateinit var courseTitleTextView: TextView
    private lateinit var courseImageView: ImageView
    private lateinit var courseTeacherNameTextView: TextView
    private lateinit var courseTeacherPictureImageView: ImageView
    private lateinit var courseDescriptionTextView: TextView
    private lateinit var courseDetailEnrollmentButton: MaterialButton
    private lateinit var courseDetailsId: String
    private lateinit var courseDetailTitle: String
    private lateinit var courseDetailImage: String
    private lateinit var courseDetailTeacherName: String
    private lateinit var courseDetailTeacherPicture: String
    private lateinit var courseDetailDescription: String
    private lateinit var courseDetailLessons: List<LessonModel>
    private lateinit var courseDetailsLessonListRecyclerView: RecyclerView
    private lateinit var courseDetailsLessonAdapter: CourseDetailsLessonsListAdapter
    private lateinit var commentsListRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val dataLayer = DataLayerSingleton

    companion object {
        const val USER_MODEL_EXTRA = "USER_MODEL_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        courseDetailEnrollmentButton = findViewById(R.id.course_detail_enrollment_button)
        courseTitleTextView = findViewById(R.id.course_detail_title)
        courseImageView = findViewById(R.id.course_detail_image)
        courseTeacherNameTextView = findViewById(R.id.course_detail_teacher_name)
        courseTeacherPictureImageView = findViewById(R.id.course_detail_teacher_picture)
        courseDescriptionTextView = findViewById(R.id.course_detail_description)
        getIntentExtraData()
        val commentViewModel = DataLayerSingleton.getCommentViewModel()

        commentViewModel.comments.observe(this) { commentsData ->
            commentsData?.let {
                val filteredComments = it.filter { comment -> comment.courseId == this.courseDetailsId }
                setupViews(this.courseDetailLessons, filteredComments)
            }
        }

        commentViewModel.getComments(sharedPreferences.getString("auth_token", null)!!)

        courseDetailEnrollmentButton.setOnClickListener {
            dataLayer.getEnrollmentViewModel().enrollCourse(sharedPreferences.getString("auth_token", null)!!, dataLayer.userConnected?.id, this.courseDetailsId)
            Intent(this, MainActivity::class.java).also {
                it.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "MyCoursesFragment")
                it.putExtra(USER_MODEL_EXTRA, dataLayer.userConnected)
                startActivity(it)
            }
        }
    }

    private fun getIntentExtraData() {
        if(this.intent.hasExtra(COURSE_MODEL_EXTRA)) {
            val courseData = intent.getParcelableExtra<CourseModel>(COURSE_MODEL_EXTRA)!!
            dataLayer.getCourseViewModel().addCourseToDB(courseData)
            this.courseDetailsId = courseData.id ?: ""
            this.courseDetailTitle = courseData.title ?: ""
            this.courseDetailImage = courseData.imageUrl ?: ""
            this.courseDetailTeacherName = courseData.teacherName ?: ""
            this.courseDetailTeacherPicture = courseData.teacherProfilePictureUrl ?: ""
            this.courseDetailDescription = courseData.description ?: ""
            this.courseDetailLessons = courseData.lessons ?: listOf()
        }
    }

    private fun setupViews(lessonsData: List<LessonModel>, commentsData: List<CommentModel>) {
        courseTitleTextView.text = this.courseDetailTitle

        Glide.with(courseImageView.context)
            .load(this.courseDetailImage)
            .placeholder(R.drawable.placeholder)
            .into(this.courseImageView)

        courseTeacherNameTextView.text = this.courseDetailTeacherName

        Glide.with(courseTeacherPictureImageView.context)
            .load(this.courseDetailTeacherPicture)
            .placeholder(R.drawable.placeholder)
            .into(this.courseTeacherPictureImageView)

        courseDescriptionTextView.text = this.courseDetailDescription

        courseDetailsLessonListRecyclerView = findViewById(R.id.courses_lessons_recycler_view)
        courseDetailsLessonAdapter = CourseDetailsLessonsListAdapter(lessonsData, this)
        courseDetailsLessonListRecyclerView.layoutManager = LinearLayoutManager(this)
        courseDetailsLessonListRecyclerView.adapter = courseDetailsLessonAdapter

        commentsListRecyclerView = findViewById(R.id.courses_comments_recycler_view)
        commentsAdapter = CommentsListAdapter(commentsData.toMutableList())
        commentsListRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsListRecyclerView.adapter = commentsAdapter
    }

    override fun setLessonVideo(lesson: LessonModel?) { }
}
