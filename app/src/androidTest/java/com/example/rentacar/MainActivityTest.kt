package com.example.rentacar

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentacar.ui.main.MainActivity
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for MainActivity
 * Tests main UI functionality including search, favorites, and navigation
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testBalanceChipDisplaysInitialBalance() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Verify balance chip shows initial balance of 500 credits
        onView(withId(R.id.balance_chip))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("500"))))

        scenario.close()
    }

    @Test
    fun testSearchFunctionality() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Type in search box
        onView(withId(R.id.search_edit_text))
            .perform(typeText("BMW"), closeSoftKeyboard())

        // Verify search box contains the text
        onView(withId(R.id.search_edit_text))
            .check(matches(withText("BMW")))

        scenario.close()
    }

    @Test
    fun testNextButtonNavigation() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Click next button to navigate to next car
        onView(withId(R.id.next_button))
            .perform(click())

        // Verify car details are still displayed after navigation
        onView(withId(R.id.car_name))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testFavoriteButtonToggle() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Click favorite button to add to favorites
        onView(withId(R.id.favorite_button))
            .perform(click())

        // Verify favorites RecyclerView is displayed
        onView(withId(R.id.favorites_recycler_view))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testRentButtonOpensDetailActivity() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Click rent button
        onView(withId(R.id.rent_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("Rent")))
            .perform(click())

        // Verify detail activity opens (car details title should be visible)
        onView(withText("Car Details"))
            .check(matches(isDisplayed()))

        scenario.close()
    }
}
