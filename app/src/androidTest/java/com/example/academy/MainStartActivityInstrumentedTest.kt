package com.example.academy

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.academy.views.activity.MainActivity
import com.example.academy.views.activity.MainStartActivity
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab

@RunWith(AndroidJUnit4::class)
class MainStartInstrumentedTest {

    @Test
    fun test_main_start_activity() {
        val scenario = ActivityScenario.launch(MainStartActivity::class.java)
        onView(withId(R.id.main_button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_register)).check(matches(isDisplayed()))
        Screengrab.screenshot("main_start")
    }

    @Test
    fun test_main_start_activity_login() {
        val scenario = ActivityScenario.launch(MainStartActivity::class.java)
        onView(withId(R.id.main_button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_register)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_login)).perform(ViewActions.click())
        onView(withId(R.id.login_email_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.login_password_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.login_submit_button)).check(matches(isDisplayed()))
        Screengrab.screenshot("login")
    }

    @Test
    fun test_screenshot_on_home_activity() {
        val scenario = ActivityScenario.launch(MainStartActivity::class.java)
        onView(withId(R.id.main_button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_register)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_login)).perform(ViewActions.click())
        onView(withId(R.id.login_email_edit_text)).perform(ViewActions.typeText("bai@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_password_edit_text)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_submit_button)).perform(ViewActions.click())
        onView(withId(R.id.home_hello_text_view)).check(matches(isDisplayed()))
        Screengrab.screenshot("home")
    }

    @Test
    fun test_screenshot_on_my_courses_activity() {
        val scenario = ActivityScenario.launch(MainStartActivity::class.java)
        onView(withId(R.id.main_button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_register)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_login)).perform(ViewActions.click())
        onView(withId(R.id.login_email_edit_text)).perform(ViewActions.typeText("bai@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_password_edit_text)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_submit_button)).perform(ViewActions.click())
        onView(withId(R.id.home_hello_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_user_connected_profile_picture)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
        Screengrab.screenshot("my_courses")
    }

    @Test
    fun test_screenshot_on_messages_activity() {
        val scenario = ActivityScenario.launch(MainStartActivity::class.java)
        onView(withId(R.id.main_button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_register)).check(matches(isDisplayed()))
        onView(withId(R.id.main_button_login)).perform(ViewActions.click())
        onView(withId(R.id.login_email_edit_text)).perform(ViewActions.typeText("bai@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_password_edit_text)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_submit_button)).perform(ViewActions.click())
        onView(withId(R.id.home_hello_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_user_connected_profile_picture)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationView)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.messages)).check(matches(isDisplayed()))
        Screengrab.screenshot("messages")
    }
}