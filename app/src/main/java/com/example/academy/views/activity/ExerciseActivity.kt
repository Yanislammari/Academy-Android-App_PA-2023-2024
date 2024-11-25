package com.example.academy.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.models.exercise.ExerciseModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.activity.CourseDetailsActivity.Companion.USER_MODEL_EXTRA
import com.example.academy.views.recycler_view.course_details_exercises_recycler_view.CourseDetailsExercisesListAdapter
import com.example.academy.views.recycler_view.course_details_exercises_recycler_view.FillInTheBlankViewHolder
import com.example.academy.views.recycler_view.course_details_exercises_recycler_view.MultipleChoiceViewHolder

class ExerciseActivity : AppCompatActivity() {
    private lateinit var courseExercisesData: List<ExerciseModel>
    private lateinit var courseData: CourseModel
    private lateinit var courseDetailsExerciseListRecyclerView: RecyclerView
    private lateinit var courseDetailsExerciseAdapter: CourseDetailsExercisesListAdapter
    private lateinit var courseDetailsExerciseTitle: TextView
    private lateinit var courseDetailsExerciseSubmitButton: Button
    private var dataLayer = DataLayerSingleton
    private var exercisesIdSuccess: MutableList<String?> = mutableListOf()
    private var note: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val EXTRA_EXERCISES = "EXTRA_EXERCISES"
        const val COURSE_DATA = "COURSE_DATA"
    }

    override fun onResume() {
        super.onResume()
        FillInTheBlankViewHolder.clearAllInputs()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercices)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        courseExercisesData = intent.getParcelableArrayListExtra(EXTRA_EXERCISES) ?: throw IllegalArgumentException("Exercises must be provided")
        courseData = intent.getParcelableExtra(COURSE_DATA)!!
        this.setupViews(courseExercisesData)

        FillInTheBlankViewHolder.initializeGoodResponses(courseExercisesData)

        this.courseDetailsExerciseSubmitButton.setOnClickListener {
            FillInTheBlankViewHolder.saveAllFillInTheBlankResponses()
            val userResponses = MultipleChoiceViewHolder.userResponses + FillInTheBlankViewHolder.userResponses
            val allResponsesProvided = courseExercisesData.all { exercise ->
                userResponses.containsKey(exercise.id) && userResponses[exercise.id]?.isNotEmpty() == true
            }

            if(!allResponsesProvided) {
                Toast.makeText(this, "Please awnser to all questions !", Toast.LENGTH_LONG).show()
            }
            else {
                this.checkMultipleChoiceExercises(MultipleChoiceViewHolder.userResponses, MultipleChoiceViewHolder.goodResponses)
                this.checkFillInTheBlankExercises(FillInTheBlankViewHolder.userResponses, FillInTheBlankViewHolder.goodResponses)
                val enrollmentId = dataLayer.getEnrollmentViewModel().getEnrollmentOfACourse(courseData.id)?.id
                if(enrollmentId != null) {
                    if(note == 0) {
                        dataLayer.getEnrollmentViewModel().correctExercises(sharedPreferences.getString("auth_token", null)!!, enrollmentId, listOf())
                    }
                    else {
                        dataLayer.getEnrollmentViewModel().correctExercises(sharedPreferences.getString("auth_token", null)!!, enrollmentId, exercisesIdSuccess.toList())
                    }
                }
                showResultDialog()
                exercisesIdSuccess.clear()
            }
        }
    }


    private fun setupViews(exercisesData: List<ExerciseModel>) {
        this.courseDetailsExerciseListRecyclerView = findViewById(R.id.exercises_recycler_view)
        this.courseDetailsExerciseAdapter = CourseDetailsExercisesListAdapter(exercisesData)
        val linearLayoutManagerCourseDetailsExerciseFormat = LinearLayoutManager(this)
        linearLayoutManagerCourseDetailsExerciseFormat.orientation = LinearLayoutManager.VERTICAL
        this.courseDetailsExerciseListRecyclerView.layoutManager = linearLayoutManagerCourseDetailsExerciseFormat
        this.courseDetailsExerciseListRecyclerView.adapter = courseDetailsExerciseAdapter
        this.courseDetailsExerciseTitle = findViewById(R.id.exercise_activity_title)
        this.courseDetailsExerciseTitle.text = "${courseData.title} - Questions"
        this.courseDetailsExerciseSubmitButton = findViewById(R.id.exercise_submit_button)
    }

    private fun checkMultipleChoiceExercises(userResponses: MutableMap<String?, String>, goodResponses: MutableMap<String?, String?>) {
        courseExercisesData.forEach { exercise ->
            if(exercise.type == "multiple_choice") {
                val userResponse = userResponses[exercise.id]
                val goodResponse = goodResponses[exercise.id]
                if(userResponse == goodResponse) {
                    note++
                    exercisesIdSuccess.add(exercise.id)
                }
            }
        }
    }

    private fun checkFillInTheBlankExercises(userResponses: MutableMap<String?, String>, goodResponses: MutableMap<String?, String?>) {
        courseExercisesData.forEach { exercise ->
            if(exercise.type == "fill_in_the_blank") {
                val userResponse = userResponses[exercise.id]
                val goodResponse = goodResponses[exercise.id]
                if(userResponse?.trim()?.equals(goodResponse?.trim(), ignoreCase = true) == true) {
                    note++
                    exercisesIdSuccess.add(exercise.id)
                }
            }
        }
    }

    private fun showResultDialog() {
        val message = "Votre note est : ${this.note}/${courseExercisesData.size}"
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Résultat")
        builder.setMessage(message)

        builder.setPositiveButton("Voir les corrections") { dialog, _ ->
            val intent = Intent(this, ExerciseCorrectionActivity::class.java).apply {
                putParcelableArrayListExtra(ExerciseCorrectionActivity.EXTRA_EXERCISES, ArrayList(courseExercisesData))
                putExtra(ExerciseCorrectionActivity.USER_RESPONSES, HashMap(MultipleChoiceViewHolder.userResponses + FillInTheBlankViewHolder.userResponses))
            }
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton("Quitter") { dialog, _ ->
            Intent(this, MainActivity::class.java).also {
                it.putExtra(MainActivity.EXTRA_FRAGMENT_NAME, "MyCoursesFragment")
                it.putExtra(USER_MODEL_EXTRA, dataLayer.userConnected)
                startActivity(it)
                dialog.dismiss()
            }
            FillInTheBlankViewHolder.clearAllInputs()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
