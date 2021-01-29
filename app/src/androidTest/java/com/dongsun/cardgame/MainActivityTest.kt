package com.dongsun.cardgame

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // Run the activity
        activityTestRule.launchActivity(Intent())
    }

    @Test
    @Throws(Exception::class)
    fun hasNoErrorEmail() {
        Espresso.onView(ViewMatchers.withId(R.id.grid_list)).check(ViewAssertions.matches(isVisible))
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.btnreset)).perform(ViewActions.click())
        Thread.sleep(2000)
    }

    companion object {
        val isVisible: Matcher<View>
            get() = object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("is visible")
                }

                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                override fun matchesSafely(view: View): Boolean {
                    return view.visibility == View.VISIBLE
                }
            }
    }
}