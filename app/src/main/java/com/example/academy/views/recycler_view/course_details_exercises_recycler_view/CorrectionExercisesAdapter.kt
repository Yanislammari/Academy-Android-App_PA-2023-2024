package com.example.academy.views.recycler_view.course_details_exercises_recycler_view

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.exercise.ExerciseModel

class CorrectionExercisesAdapter(private val exercises: List<ExerciseModel>, private val userResponses: MutableMap<String?, String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_MULTIPLE_CHOICE = 1
        private const val TYPE_FILL_IN_THE_BLANK = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(exercises[position].type) {
            "multiple_choice" -> TYPE_MULTIPLE_CHOICE
            "fill_in_the_blank" -> TYPE_FILL_IN_THE_BLANK
            else -> throw IllegalArgumentException("Invalid exercise type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_MULTIPLE_CHOICE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.correction_exercise_cell, parent, false)
                MultipleChoiceViewHolder(view)
            }
            TYPE_FILL_IN_THE_BLANK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.correction_exercise_blank_cell, parent, false)
                FillInTheBlankViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exercise = exercises[position]

        when(holder) {
            is MultipleChoiceViewHolder -> holder.bind(exercise, userResponses[exercise.id])
            is FillInTheBlankViewHolder -> holder.bind(exercise, userResponses[exercise.id])
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class MultipleChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTitle: TextView = itemView.findViewById(R.id.question_title)
        private val option1: TextView = itemView.findViewById(R.id.question_text_option1)
        private val option2: TextView = itemView.findViewById(R.id.question_text_option2)
        private val option3: TextView = itemView.findViewById(R.id.question_text_option3)
        private val option4: TextView = itemView.findViewById(R.id.question_text_option4)

        fun bind(exercise: ExerciseModel, userResponse: String?) {
            questionTitle.text = exercise.title
            val options = listOf(option1, option2, option3, option4)

            options.forEachIndexed { index, option ->
                val optionText = exercise.options?.get(index)
                option.text = optionText

                if(optionText == exercise.correctOption) {
                    option.setTextColor(Color.parseColor("#32CD32"))
                    option.setTypeface(null, android.graphics.Typeface.BOLD)
                }

                if(optionText == userResponse && userResponse != exercise.correctOption) {
                    option.setTextColor(Color.RED)
                    option.setTypeface(null, android.graphics.Typeface.BOLD)
                }
            }
        }
    }

    class FillInTheBlankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTitle: TextView = itemView.findViewById(R.id.question_title)
        private val answerText: TextView = itemView.findViewById(R.id.question_text_option)
        private val userAnswerText: TextView = itemView.findViewById(R.id.question_text_option_false)

        fun bind(exercise: ExerciseModel, userResponse: String?) {
            questionTitle.text = exercise.title
            val correctAnswer = exercise.correctOption ?: ""
            val correctAnswerSpannable = SpannableString(correctAnswer)

            correctAnswerSpannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#32CD32")), 0, correctAnswer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            correctAnswerSpannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, correctAnswer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            answerText.text = correctAnswerSpannable

            if(userResponse != correctAnswer) {
                userAnswerText.text = userResponse
                userAnswerText.setTextColor(Color.RED)
                userAnswerText.setTypeface(null, android.graphics.Typeface.BOLD)
                userAnswerText.visibility = View.VISIBLE
            }
            else {
                userAnswerText.visibility = View.GONE
            }
        }
    }
}
