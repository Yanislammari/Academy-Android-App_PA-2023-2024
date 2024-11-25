package com.example.academy.views.recycler_view.course_details_exercises_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.exercise.ExerciseModel

class CourseDetailsExercisesListAdapter(private var courseDetailsExerciseList: List<ExerciseModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_MULTIPLE_CHOICE = 1
        const val TYPE_FILL_IN_THE_BLANK = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(courseDetailsExerciseList[position].type) {
            "multiple_choice" -> TYPE_MULTIPLE_CHOICE
            "fill_in_the_blank" -> TYPE_FILL_IN_THE_BLANK
            else -> TYPE_MULTIPLE_CHOICE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_MULTIPLE_CHOICE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_cell, parent, false)
                MultipleChoiceViewHolder(view)
            }
            TYPE_FILL_IN_THE_BLANK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_blank_cell, parent, false)
                FillInTheBlankViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exercise = courseDetailsExerciseList[position]
        when(holder.itemViewType) {
            TYPE_MULTIPLE_CHOICE -> (holder as MultipleChoiceViewHolder).bind(exercise)
            TYPE_FILL_IN_THE_BLANK -> (holder as FillInTheBlankViewHolder).bind(exercise)
        }
    }

    override fun getItemCount(): Int {
        return courseDetailsExerciseList.size
    }
}
