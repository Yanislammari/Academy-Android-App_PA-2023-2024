package com.example.academy.views.recycler_view.enrolled_course_details_lessons_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.lesson.LessonModel
import com.example.academy.views.click_listeners.LessonOnClickListener

class EnrolledCourseDetailsLessonsListAdapter(private var courseDetailsLessonList: List<LessonModel>, private val courseClickHandler: LessonOnClickListener, private val finishedLessons: List<String>) : RecyclerView.Adapter<EnrolledCourseDetailsLessonViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledCourseDetailsLessonViewHolder {
        val courseDetailsLessonsView = LayoutInflater.from(parent.context).inflate(R.layout.lesson_cell, parent, false)
        return EnrolledCourseDetailsLessonViewHolder(courseDetailsLessonsView)
    }

    override fun getItemCount(): Int {
        return this.courseDetailsLessonList.size
    }

    override fun onBindViewHolder(holder: EnrolledCourseDetailsLessonViewHolder, position: Int) {
        val currentCourseDetailsLessonData = this.courseDetailsLessonList[position] // Get the data at the right position
        holder.bind(currentCourseDetailsLessonData)

        when {
            selectedPosition == position -> {
                holder.itemView.setBackgroundResource(R.drawable.lesson_cell_selected_background)
            }
            finishedLessons.contains(currentCourseDetailsLessonData.id) -> {
                holder.itemView.setBackgroundResource(R.drawable.lesson_cell_finish_background)
            }
            else -> {
                holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
            }
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            courseClickHandler.setLessonVideo(currentCourseDetailsLessonData)
        }
    }
}
