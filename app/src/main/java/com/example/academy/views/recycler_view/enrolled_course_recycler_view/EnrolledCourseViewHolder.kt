package com.example.academy.views.recycler_view.enrolled_course_recycler_view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.models.enrollment.EnrollmentModel
import com.example.academy.utils.singleton.DataLayerSingleton

class EnrolledCourseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var enrolledCourseTitle: TextView = itemView.findViewById(R.id.enroled_course_title)
    private var enrolledCourseImage: ImageView = itemView.findViewById(R.id.enrolled_course_image)
    private var enrolledCourseTeacherName: TextView = itemView.findViewById(R.id.enrolled_course_teacher_name)
    private var enrolledCourseTeacherProfilePicture: ImageView = itemView.findViewById(R.id.enrolled_course_teacher_pp)
    private var enrolledCourseLessonNumber: TextView = itemView.findViewById(R.id.enrolled_course_lesson_nb)
    private var enrolledCourseLevel: TextView = itemView.findViewById(R.id.enrolled_course_level)
    private var enrolledCourseProgressBar: ProgressBar = itemView.findViewById(R.id.enrolled_course_progress_bar)
    private var enrolledCourseDeleteButton: ImageButton = itemView.findViewById(R.id.enrolled_course_delete_button)
    private lateinit var sharedPreferences: SharedPreferences

    fun bind(enrolledCourse: CourseModel?, enrollment: EnrollmentModel?) {
        sharedPreferences = itemView.context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        enrolledCourseDeleteButton.setOnClickListener {
            DataLayerSingleton.getEnrollmentViewModel().deleteEnrollment(sharedPreferences.getString("auth_token", null)!!, enrollment?.id)
        }

        this.enrolledCourseTitle.text = enrolledCourse?.title ?: "Title not available"

        Glide.with(itemView.context)
            .load(enrolledCourse?.imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.enrolledCourseImage)

        this.enrolledCourseTeacherName.text = enrolledCourse?.teacherName ?: "Name not available"

        Glide.with(itemView.context)
            .load(enrolledCourse?.teacherProfilePictureUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.enrolledCourseTeacherProfilePicture)

        this.enrolledCourseLessonNumber.text = "${enrollment?.progression?.lessonsFinish?.size} / ${enrolledCourse?.lessons?.size} Lessons"

        when(enrolledCourse?.level) {
            "beginner" -> this.enrolledCourseLevel.text = "Beginner"
            "intermediate" -> this.enrolledCourseLevel.text = "Intermediate"
            "advanced" -> this.enrolledCourseLevel.text = "Advanced"
        }

        this.enrolledCourseProgressBar.max = enrolledCourse?.lessons?.size ?: 0
        this.enrolledCourseProgressBar.progress = enrollment?.progression?.lessonsFinish?.size ?: 0
    }
}
