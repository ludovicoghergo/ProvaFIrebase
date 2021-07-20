package com.example.provafirebase

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.provafirebase.singleGroup.SingleGroupActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SingleGroupActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SingleGroupActivity::class.java)

    @Before
    fun waitDb() {
        Thread.sleep(3000)
    }

    @Test
    fun verifyInvoiceNumber() {
        Espresso.onView(ViewMatchers.withId(R.id.lista_spese))
            .check(ViewAssertions.matches(ViewMatchers.hasChildCount(2)))
    }

    @Test
    fun verifyInvoice() {
        Espresso.onView(ViewMatchers.withText("test 2"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }



}