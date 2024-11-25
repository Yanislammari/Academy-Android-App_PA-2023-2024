package com.example.academy.views.recycler_view.course_details_lessons_recycler_view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.lesson.LessonModel

class CourseDetailsLessonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var courseDetailsLessonTitle: TextView = itemView.findViewById(R.id.course_lesson_name_tv)

    fun bind(courseDetailsLesson: LessonModel) {
        this.courseDetailsLessonTitle.text = courseDetailsLesson.title
    }
}
