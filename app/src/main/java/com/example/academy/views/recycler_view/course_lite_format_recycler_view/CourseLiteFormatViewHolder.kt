package com.example.academy.views.recycler_view.course_lite_format_recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.course.CourseModel

class CourseLiteFormatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var courseLiteFormatTitle: TextView = itemView.findViewById(R.id.course_lite_format_title)
    private var courseLiteFormatImage: ImageView = itemView.findViewById(R.id.course_lite_format_image)
    private var courseLiteFormatTeacherName: TextView = itemView.findViewById(R.id.course_lite_format_teacher_name)
    private var courseLiteFormatTeacherProfilePicture: ImageView = itemView.findViewById(R.id.course_lite_format_teacher_pp)
    private var courseLiteFormatLessonNumber: TextView = itemView.findViewById(R.id.course_lite_format_lesson_nb)
    private var courseLiteFormatLevel: TextView = itemView.findViewById(R.id.course_lite_format_level)

    fun bind(courseLiteFormat: CourseModel) {
        this.courseLiteFormatTitle.text = courseLiteFormat.title

        Glide.with(itemView.context)
            .load(courseLiteFormat.imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.courseLiteFormatImage)

        this.courseLiteFormatTeacherName.text = courseLiteFormat.teacherName

        Glide.with(itemView.context)
            .load(courseLiteFormat.teacherProfilePictureUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.courseLiteFormatTeacherProfilePicture)

        this.courseLiteFormatLessonNumber.text = "${courseLiteFormat.lessons?.size} Lessons"

        when(courseLiteFormat.level) {
            "beginner" -> this.courseLiteFormatLevel.text = "Beginner"
            "intermediate" -> this.courseLiteFormatLevel.text = "Intermediate"
            "advanced" -> this.courseLiteFormatLevel.text = "Advanced"
        }
    }
}
