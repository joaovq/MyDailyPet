package br.com.joaovq.mydailypet.pet.presentation.view

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.launchFragmentInHiltContainer
import br.com.joaovq.mydailypet.pet.presentation.adapter.DetailPetViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class PetFragmentTest {

    @Before
    fun setup() {
        launchFragmentInHiltContainer<PetFragment>(
            fragmentArgs = bundleOf(
                "id" to 1,
            ),
        )
    }

    @Test
    fun clickPhotoPet(): Unit = runBlocking {
        onView(withId(R.id.layt_photo_pet))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun clickItemAttachRecyclerView(): Unit = runBlocking {
        delay(2000)
        onView(withId(R.id.rv_details_pet))
            .check(matches(isDisplayed()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<DetailPetViewHolder>(
                    0,
                    click(),
                ),
            )
    }

    @Test
    fun clickShareLayout(): Unit = runBlocking {
        onView(withId(R.id.sv_pet_screen))
            .check(matches(isDisplayed()))
            .perform(swipeUp())
        onView(withId(R.id.item_share_pet))
            .check(matches(isDisplayed()))
            .inRoot(RootMatchers.isTouchable())
            .withFailureHandler { error, _ ->
                error.printStackTrace()
            }.perform(
                click(),
            )
    }
}
