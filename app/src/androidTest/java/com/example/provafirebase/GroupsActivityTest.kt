package com.example.provafirebase

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroupsActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(GroupsActivity::class.java)

    @Before
    fun waitDb(){
        Thread.sleep(3000)
    }

    @Test
    fun verifyGroupExist(){
        Espresso.onView((ViewMatchers.withText("Test Group")))
            .check(ViewAssertions.matches(
            ViewMatchers.isDisplayed()
        ))
    }

    @Test
    fun welcomeMessage(){
        Espresso.onView((ViewMatchers.withId(R.id.welcome_txt)))
            .check(ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            ))
    }
}