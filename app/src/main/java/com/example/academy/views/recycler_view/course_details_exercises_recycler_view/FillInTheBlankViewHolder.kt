package com.example.academy.views.recycler_view.course_details_exercises_recycler_view

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.exercise.ExerciseModel

class FillInTheBlankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var questionTitle: TextView = itemView.findViewById(R.id.question_title)
    private var answerInput: EditText = itemView.findViewById(R.id.question_input)

    companion object {
        val userResponses: MutableMap<String?, String> = mutableMapOf()
        val goodResponses: MutableMap<String?, String?> = mutableMapOf()
        private val fillInTheBlankViewHolders: MutableList<FillInTheBlankViewHolder> = mutableListOf()

        fun saveAllFillInTheBlankResponses() {
            fillInTheBlankViewHolders.forEach { holder ->
                val exerciseId = holder.itemView.tag as String?
                val userResponse = holder.answerInput.text.toString().trim()

                if(exerciseId != null) {
                    userResponses[exerciseId] = userResponse
                }
            }
        }

        fun initializeGoodResponses(exercises: List<ExerciseModel>) {
            goodResponses.clear()

            exercises.forEach { exercise ->
                if(exercise.type == "fill_in_the_blank") {
                    goodResponses[exercise.id] = exercise.correctOption
                }
            }
        }

        fun clearAllInputs() {
            fillInTheBlankViewHolders.forEach { holder ->
                holder.answerInput.text.clear()
            }
        }
    }

    init {
        fillInTheBlankViewHolders.add(this)
    }

    fun bind(exercise: ExerciseModel) {
        questionTitle.text = exercise.title
        itemView.tag = exercise.id
    }
}
