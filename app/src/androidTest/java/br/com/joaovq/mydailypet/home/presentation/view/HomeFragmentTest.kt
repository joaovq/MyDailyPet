package br.com.joaovq.mydailypet.home.presentation.view

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import br.com.joaovq.mydailypet.MainActivity
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter
import br.com.joaovq.mydailypet.launchFragmentInHiltContainer
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickAddPet() {
        onView(ViewMatchers.withId(R.id.lt_add_pet_category)).check(matches(isDisplayed()))
            .perform(scrollTo())
            .perform(click())
    }

    @Test
    fun clickNavigateToPetDetails(): Unit = runBlocking {
        delay(3000)
        onView(ViewMatchers.withId(R.id.rv_my_pets_list)).check(matches(isDisplayed()))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PetsListAdapter.PetsListViewHolder>(
                        0,
                        longClick(),
                    ),
            )
        delay(1000)
        onView(
            ViewMatchers.withText(R.string.see_details_pet_item_menu),
        ).check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun clickInItemRecyclerView(): Unit = runBlocking {
        delay(3000)
        onView(ViewMatchers.withId(R.id.rv_my_pets_list)).check(matches(isDisplayed()))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PetsListAdapter.PetsListViewHolder>(
                        0,
                        longClick(),
                    ),
            )
    }

    @Test
    fun testNavigateClickAddPet() {
        val testNavHostController =
            TestNavHostController(ApplicationProvider.getApplicationContext())
        val fragmentScenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_MyDailyPet) {
                testNavHostController.setGraph(R.navigation.main_graph)
                testNavHostController.setCurrentDestination(R.id.addPetFragment)
                Navigation.setViewNavController(requireView(), testNavHostController)
            }
        onView(ViewMatchers.withId(R.id.lt_add_pet_category)).check(matches(isDisplayed()))
            .perform(scrollTo())
            .perform(click())
        TestCase.assertEquals(R.id.addPetFragment, testNavHostController.currentDestination?.id)
    }
}
