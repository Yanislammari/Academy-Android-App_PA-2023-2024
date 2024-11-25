package com.example.academy

import com.example.academy.utils.top_level_functions.isPasswordValid
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordFunctionsUnitTest {

    @Test
    fun testPasswordWithAllValidRequirements() {
        val validPassword = "Password1@"
        assertTrue(isPasswordValid(validPassword))
    }

    @Test
    fun testPasswordWithNoDigit() {
        val invalidPassword = "Password@"
        assertFalse(isPasswordValid(invalidPassword))
    }

    @Test
    fun testPasswordWithNoUppercaseLetter() {
        val invalidPassword = "password1@"
        assertFalse(isPasswordValid(invalidPassword))
    }

    @Test
    fun testPasswordWithNoSpecialCharacter() {
        val invalidPassword = "Password1"
        assertFalse(isPasswordValid(invalidPassword))
    }

    @Test
    fun testPasswordWithLessThanEightCharacters() {
        val invalidPassword = "Pwd1@"
        assertFalse(isPasswordValid(invalidPassword))
    }

    @Test
    fun testPasswordWithEmptyString() {
        val invalidPassword = ""
        assertFalse(isPasswordValid(invalidPassword))
    }

    @Test
    fun testPasswordWithOnlySpaces() {
        val invalidPassword = "       "
        assertFalse(isPasswordValid(invalidPassword))
    }
}