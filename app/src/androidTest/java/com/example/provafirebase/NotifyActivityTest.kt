package com.example.provafirebase



import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotifyActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(NotificationActivity::class.java)

    @Before
    fun waitDb() {
        Thread.sleep(3000)
    }

    @Test
    fun verifyNotificationNumber() {
        onView(withId(R.id.notifiche_lista))
            .check(matches(hasChildCount(2)))
    }

    @Test
    fun verifyNotification() {
        onView(withText("è stata aggiunta la nuova spesa test invoice"))
            .check(matches(isDisplayed()))
    }


}