package com.example.academy.utils.top_level_functions

fun isPasswordValid(password: String): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$"
    val passwordMatcher = Regex(passwordPattern)
    return passwordMatcher.find(password) != null
}
