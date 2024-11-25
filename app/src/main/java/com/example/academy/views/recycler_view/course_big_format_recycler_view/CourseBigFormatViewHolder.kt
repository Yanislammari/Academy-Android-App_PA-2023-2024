package com.example.academy.views.recycler_view.course_big_format_recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.course.CourseModel

class CourseBigFormatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var courseBigFormatTitle: TextView = itemView.findViewById(R.id.course_big_format_title)
    private var courseBigFormatImage: ImageView = itemView.findViewById(R.id.course_big_format_image)
    private var courseBigFormatTeacherName: TextView = itemView.findViewById(R.id.course_big_format_teacher_name)
    private var courseBigFormatTeacherProfilePicture: ImageView = itemView.findViewById(R.id.course_big_format_teacher_pp)
    private var courseBigFormatLessonNumber: TextView = itemView.findViewById(R.id.course_big_format_lesson_nb)
    private var courseBigFormatLevel: TextView = itemView.findViewById(R.id.course_big_format_level)

    fun bind(courseBigFormat: CourseModel) {
        this.courseBigFormatTitle.text = courseBigFormat.title

        Glide.with(itemView.context)
            .load(courseBigFormat.imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.courseBigFormatImage)

        this.courseBigFormatTeacherName.text = courseBigFormat.teacherName

        Glide.with(itemView.context)
            .load(courseBigFormat.teacherProfilePictureUrl)
            .placeholder(R.drawable.placeholder)
            .into(this.courseBigFormatTeacherProfilePicture)

        this.courseBigFormatLessonNumber.text = "${courseBigFormat.lessons?.size} Lessons"

        when(courseBigFormat.level){
            "beginner" -> this.courseBigFormatLevel.text = "Beginner"
            "intermediate" -> this.courseBigFormatLevel.text = "Intermediate"
            "advanced" -> this.courseBigFormatLevel.text = "Advanced"
        }
    }
}
