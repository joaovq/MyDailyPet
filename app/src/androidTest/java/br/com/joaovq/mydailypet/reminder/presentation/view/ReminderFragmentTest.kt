package br.com.joaovq.mydailypet.reminder.presentation.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class ReminderFragmentTest {
    @Before
    fun setUp() {
        launchFragmentInHiltContainer<ReminderFragment>()
    }

    @Test
    fun typeTextEditReminder() {
        onView(withId(R.id.et_name_reminder)).check(matches(isDisplayed()))
            .perform(ViewActions.typeText("My new name"))
    }
}
