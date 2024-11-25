package com.example.academy.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.comments.CommentModel
import com.example.academy.models.course.CourseModel
import com.example.academy.models.enrollment.EnrollmentModel
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.models.lesson.LessonModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.CommentViewModel
import com.example.academy.views.fragment.HomeFragment.Companion.COURSE_MODEL_EXTRA
import com.example.academy.views.click_listeners.LessonOnClickListener
import com.example.academy.views.recycler_view.enrolled_course_details_lessons_recycler_view.EnrolledCourseDetailsLessonsListAdapter
import com.google.android.material.button.MaterialButton

class EnrolledCourseDetailsActivity : AppCompatActivity(), LessonOnClickListener {
    private lateinit var enrolledCourseTitleTextView: TextView
    private lateinit var enrolledCourseLessonVideoView: VideoView
    private lateinit var enrolledCourseTeacherNameTextView: TextView
    private lateinit var enrolledCourseTeacherPictureImageView: ImageView
    private lateinit var enrolledCourseDescriptionTextView: TextView
    private lateinit var enrolledCourseDetailEnrollmentButton: MaterialButton
    private lateinit var enrolledCourseExerciseButton: CardView
    private lateinit var enrolledCourseExerciseProgressBar: ProgressBar
    private lateinit var enrolledCourseDetailsId: String
    private lateinit var enrolledCourseDetailTitle: String
    private lateinit var enrolledCourseLessonVideoUrl: String
    private lateinit var enrolledCourseDetailTeacherName: String
    private lateinit var enrolledCourseDetailTeacherPicture: String
    private lateinit var enrolledCourseDetailDescription: String
    private lateinit var enrolledCourseDetailLessons: List<LessonModel>
    private lateinit var finishedLessons: List<String>
    private lateinit var enrolledCourseDetailsExercises: ArrayList<ExerciseModel>
    private lateinit var enrolledCourseDetailsLessonListRecyclerView: RecyclerView
    private lateinit var enrolledCourseDetailsLessonAdapter: EnrolledCourseDetailsLessonsListAdapter
    private var enrollment: EnrollmentModel? = null
    private val dataLayer = DataLayerSingleton
    private var lessonId: String? = null
    private lateinit var commentInput: EditText
    private lateinit var postCommentButton: Button
    private val commentViewModel: CommentViewModel = DataLayerSingleton.getCommentViewModel()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataLayer.getEnrollmentViewModel().errorMessage.postValue(null)
        setContentView(R.layout.activity_enrolled_course_details)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        getIntentExtraData()
        observeViewModel()
        this.setupViews(this.enrolledCourseDetailLessons, this.enrolledCourseDetailsExercises)

        commentInput = findViewById(R.id.comment_input)
        postCommentButton = findViewById(R.id.post_comment_button)

        postCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()

            if(commentText.isNotBlank()) {
                val comment = CommentModel(
                    id = "",
                    userId = dataLayer.userConnected?.id ?: "",
                    courseId = intent.getParcelableExtra<CourseModel>(COURSE_MODEL_EXTRA)!!.id,
                    content = commentText,
                    timestamp = null
                )
                commentViewModel.postComment(sharedPreferences.getString("auth_token", null)!!, comment)
                Toast.makeText(this, "Comment posted!", Toast.LENGTH_SHORT).show()
                commentInput.text.clear()
            }
            else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }

        this.enrolledCourseDetailEnrollmentButton.setOnClickListener {
            if(lessonId == null) {
                Toast.makeText(this, "Select a lesson to mark it as completed!", Toast.LENGTH_LONG).show()
            }
            else {
                dataLayer.getEnrollmentViewModel().finishLesson(sharedPreferences.getString("auth_token", null)!!, dataLayer.getEnrollmentViewModel().getEnrollmentOfACourse(this.enrolledCourseDetailsId)?.id, lessonId)
                Intent(this, MainActivity::class.java).also {
                    it.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "MyCoursesFragment")
                    it.putExtra(CourseDetailsActivity.USER_MODEL_EXTRA, dataLayer.userConnected)
                    startActivity(it)
                }
            }
        }

        this.enrolledCourseExerciseButton.setOnClickListener {
            Intent(this, ExerciseActivity::class.java).also {
                it.putParcelableArrayListExtra(ExerciseActivity.EXTRA_EXERCISES, this.enrolledCourseDetailsExercises)
                it.putExtra(ExerciseActivity.COURSE_DATA, intent.getParcelableExtra<CourseModel>(COURSE_MODEL_EXTRA)!!)
                startActivity(it)
            }
        }
    }

    private fun getIntentExtraData() {
        if(this.intent.hasExtra(COURSE_MODEL_EXTRA)) {
            val courseData = intent.getParcelableExtra<CourseModel>(COURSE_MODEL_EXTRA)!!

            this.enrolledCourseDetailsId = courseData.id ?: ""
            this.enrolledCourseDetailTitle = courseData.title ?: ""
            this.enrolledCourseLessonVideoUrl = courseData.lessons?.get(0)?.video ?: ""
            this.enrolledCourseDetailTeacherName = courseData.teacherName ?: ""
            this.enrolledCourseDetailTeacherPicture = courseData.teacherProfilePictureUrl ?: ""
            this.enrolledCourseDetailDescription = courseData.description ?: ""
            this.enrolledCourseDetailLessons = courseData.lessons ?: listOf()
            this.enrolledCourseDetailsExercises = courseData.exercises as ArrayList<ExerciseModel>

            enrollment = dataLayer.getEnrollmentViewModel().getEnrollmentOfACourse(this.enrolledCourseDetailsId)
            this.finishedLessons = enrollment?.progression?.lessonsFinish ?: listOf()
        }
    }

    private fun setupViews(lessonsData: List<LessonModel>, exercisesData: List<ExerciseModel>) {
        this.enrolledCourseTitleTextView = findViewById(R.id.enrolled_course_detail_title)
        this.enrolledCourseTitleTextView.text = this.enrolledCourseDetailTitle

        this.enrolledCourseLessonVideoView = findViewById(R.id.enrolled_course_detail_video_lesson)
        setupVideoView(this.enrolledCourseLessonVideoView, this.enrolledCourseLessonVideoUrl)

        this.enrolledCourseTeacherNameTextView = findViewById(R.id.enrolled_course_detail_teacher_name)
        this.enrolledCourseTeacherNameTextView.text = this.enrolledCourseDetailTeacherName

        this.enrolledCourseTeacherPictureImageView = findViewById(R.id.enrolled_course_detail_teacher_picture)
        Glide.with(enrolledCourseTeacherPictureImageView.context)
            .load(this.enrolledCourseDetailTeacherPicture)
            .placeholder(R.drawable.placeholder)
            .into(this.enrolledCourseTeacherPictureImageView)

        this.enrolledCourseDescriptionTextView = findViewById(R.id.enrolled_course_detail_description)
        this.enrolledCourseDescriptionTextView.text = this.enrolledCourseDetailDescription

        this.enrolledCourseDetailEnrollmentButton = findViewById(R.id.enrolled_course_detail_finish_lesson_button)

        this.enrolledCourseDetailsLessonListRecyclerView = findViewById(R.id.enrolled_courses_lessons_recycler_view)

        this.enrolledCourseDetailsLessonAdapter = EnrolledCourseDetailsLessonsListAdapter(lessonsData, this, finishedLessons)
        val linearLayoutManagerCourseDetailsLessonFormat = LinearLayoutManager(this)
        linearLayoutManagerCourseDetailsLessonFormat.orientation = LinearLayoutManager.VERTICAL
        this.enrolledCourseDetailsLessonListRecyclerView.layoutManager = linearLayoutManagerCourseDetailsLessonFormat
        this.enrolledCourseDetailsLessonListRecyclerView.adapter = enrolledCourseDetailsLessonAdapter

        this.enrolledCourseExerciseButton = findViewById(R.id.enrolled_course_detail_exercises_button)

        this.enrolledCourseExerciseProgressBar = findViewById(R.id.enrolled_course_exercises_progress_bar)
        this.enrolledCourseExerciseProgressBar.progress = enrollment?.progression?.exercisesSucces?.size!!
        this.enrolledCourseExerciseProgressBar.max = exercisesData.size
    }

    private fun setupVideoView(videoView: VideoView, videoUrl: String) {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(videoUrl))
        videoView.setOnPreparedListener { mediaPlayer ->
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight
            val videoViewWidth = videoView.width
            val videoViewHeight = videoView.height

            val xScale = videoViewWidth.toFloat() / videoWidth
            val yScale = videoViewHeight.toFloat() / videoHeight
            val scale = xScale.coerceAtLeast(yScale)

            val scaledWidth = (scale * videoWidth).toInt()
            val scaledHeight = (scale * videoHeight).toInt()

            val layoutParams = videoView.layoutParams
            layoutParams.width = scaledWidth
            layoutParams.height = scaledHeight
            videoView.layoutParams = layoutParams
        }
        videoView.requestFocus()
        videoView.start()
    }

    private fun observeViewModel() {
        this.dataLayer.getEnrollmentViewModel().errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setLessonVideo(lesson: LessonModel?) {
        this.lessonId = lesson?.id
        this.enrolledCourseLessonVideoUrl = lesson?.video.toString()
        setupVideoView(this.enrolledCourseLessonVideoView, this.enrolledCourseLessonVideoUrl)
        this.enrolledCourseDescriptionTextView.text = lesson?.description
    }
}
