package com.example.academy.views.recycler_view.course_details_lessons_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.lesson.LessonModel
import com.example.academy.views.click_listeners.LessonOnClickListener

class CourseDetailsLessonsListAdapter(private var courseDetailsLessonList: List<LessonModel>, private val courseClickHandler: LessonOnClickListener) : RecyclerView.Adapter<CourseDetailsLessonsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailsLessonsViewHolder {
        val courseDetailsLessonsView = LayoutInflater.from(parent.context).inflate(R.layout.lesson_cell, parent, false)
        return CourseDetailsLessonsViewHolder(courseDetailsLessonsView)
    }

    override fun getItemCount(): Int {
        return this.courseDetailsLessonList.size
    }

    override fun onBindViewHolder(holder: CourseDetailsLessonsViewHolder, position: Int) {
        val currentCourseDetailsLessonData = this.courseDetailsLessonList[position]
        holder.bind(currentCourseDetailsLessonData)

        holder.itemView.setOnClickListener {
            courseClickHandler.setLessonVideo(currentCourseDetailsLessonData)
        }
    }
}
