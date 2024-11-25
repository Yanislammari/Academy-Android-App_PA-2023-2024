package com.example.academy.views.recycler_view.course_details_exercises_recycler_view

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.exercise.ExerciseModel

class MultipleChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var questionTitle: TextView = itemView.findViewById(R.id.question_title)
    private var option1: RadioButton = itemView.findViewById(R.id.question_radio_option1)
    private var option2: RadioButton = itemView.findViewById(R.id.question_radio_option2)
    private var option3: RadioButton = itemView.findViewById(R.id.question_radio_option3)
    private var option4: RadioButton = itemView.findViewById(R.id.question_radio_option4)

    fun bind(exercise: ExerciseModel) {
        questionTitle.text = exercise.title
        option1.text = exercise.options?.get(0) ?: "Option 1"
        option2.text = exercise.options?.get(1) ?: "Option 2"
        option3.text = exercise.options?.get(2) ?: "Option 3"
        option4.text = exercise.options?.get(3) ?: "Option 4"

        MultipleChoiceViewHolder.goodResponses[exercise.id] = exercise.correctOption

        option1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                MultipleChoiceViewHolder.userResponses[exercise.id] = option1.text.toString()
            }
        }
        option2.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                MultipleChoiceViewHolder.userResponses[exercise.id] = option2.text.toString()
            }
        }
        option3.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                MultipleChoiceViewHolder.userResponses[exercise.id] = option3.text.toString()
            }
        }
        option4.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                MultipleChoiceViewHolder.userResponses[exercise.id] = option4.text.toString()
            }
        }
    }

    companion object {
        val userResponses: MutableMap<String?, String> = mutableMapOf()
        val goodResponses: MutableMap<String?, String?> = mutableMapOf()
    }
}
