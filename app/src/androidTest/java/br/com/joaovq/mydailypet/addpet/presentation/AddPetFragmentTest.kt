package br.com.joaovq.mydailypet.addpet.presentation

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.EditorAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.ViewPagerActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.addpet.presentation.view.AddPetFragment
import br.com.joaovq.mydailypet.launchFragmentInHiltContainer
import com.google.android.material.datepicker.MaterialDatePicker
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
internal class AddPetFragmentTest {

    @Before
    fun setup() {
        launchFragmentInHiltContainer<AddPetFragment>()
    }

    @Test
    fun typeTextInName() {
        onView(withId(R.id.sv_add_pet))
            .perform(swipeUp())
        onView(withId(R.id.et_name_add_pet))
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText("Melquis"))
            .check(matches(withText("Melquis")))
    }

    @Test
    fun typeTextInBreed() {
        onView(withId(R.id.sv_add_pet))
            .perform(swipeUp())
        onView(withId(R.id.atctv_bredd_add_pet))
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText("Schnauzer"))
        onView(withId(R.id.atctv_bredd_add_pet))
            .check(matches(withText(R.string.text_schnauzer)))
    }
}
