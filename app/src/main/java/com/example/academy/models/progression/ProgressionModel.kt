package com.example.academy.models.progression

data class ProgressionModel(
    val lessonsFinish: List<String>,
    val exercisesSucces: List<String>,
    val attempts: Int
)
