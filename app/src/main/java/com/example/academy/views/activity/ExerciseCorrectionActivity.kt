package com.example.academy.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.views.recycler_view.course_details_exercises_recycler_view.CorrectionExercisesAdapter

class ExerciseCorrectionActivity : AppCompatActivity() {
    private lateinit var correctionRecyclerView: RecyclerView
    private lateinit var exercisesData: List<ExerciseModel>
    private lateinit var userResponses: MutableMap<String?, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_correction)

        exercisesData = intent.getParcelableArrayListExtra(EXTRA_EXERCISES) ?: throw IllegalArgumentException("Exercises must be provided")
        userResponses = intent.getSerializableExtra(USER_RESPONSES) as? MutableMap<String?, String> ?: mutableMapOf()

        correctionRecyclerView = findViewById(R.id.correction_exercises_recycler_view)
        val correctionAdapter = CorrectionExercisesAdapter(exercisesData, userResponses)
        correctionRecyclerView.layoutManager = LinearLayoutManager(this)
        correctionRecyclerView.adapter = correctionAdapter
    }

    companion object {
        const val EXTRA_EXERCISES = "EXTRA_EXERCISES"
        const val USER_RESPONSES = "USER_RESPONSES"
    }
}
