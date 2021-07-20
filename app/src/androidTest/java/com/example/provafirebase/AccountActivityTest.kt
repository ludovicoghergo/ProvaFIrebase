package com.example.provafirebase

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule
import java.lang.Thread.sleep

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AccountActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(AccountActivity::class.java)

    @Before
    fun waitDb(){
        sleep(3000)
    }

    @Test
    fun verifyGroupNumber(){
        Espresso.onView((ViewMatchers.withId(R.id.groups_txt))).check(ViewAssertions.matches(ViewMatchers.withText("1")))
    }

    @Test
    fun verifyUpdateNumber(){
        Espresso.onView((ViewMatchers.withId(R.id.news_txt))).check(ViewAssertions.matches(ViewMatchers.withText("2")))
    }

    @Test
    fun verifyUserData(){
        Espresso.onView((ViewMatchers.withId(R.id.userName)))
            .check(ViewAssertions.matches(ViewMatchers.withText("test firebase")))

        Espresso.onView((ViewMatchers.withId(R.id.userEmail)))
            .check(ViewAssertions.matches(ViewMatchers.withText("testfirebase947@gmail.com")))
    }

}