package com.example.academy.models.exercise

sealed class ExerciseRequests {
    data class ExercisesIdRequest(
        val exercisesId: List<String?>
    )
}
