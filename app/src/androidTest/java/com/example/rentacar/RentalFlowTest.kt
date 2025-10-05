package com.example.rentacar

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentacar.ui.main.MainActivity
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for complete rental flow
 * Tests the integration between MainActivity and DetailActivity
 */
@RunWith(AndroidJUnit4::class)
class RentalFlowTest {

    @Test
    fun testCompleteRentalFlowWithCancel() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Verify initial balance
        onView(withId(R.id.balance_chip))
            .check(matches(withText(containsString("500"))))

        // Click rent button to open detail activity
        onView(withId(R.id.rent_button))
            .perform(click())

        // Verify we're in detail activity
        onView(withText("Car Details"))
            .check(matches(isDisplayed()))

        // Click cancel button to return to main
        onView(withId(R.id.cancel_button))
            .perform(click())

        // Should be back in main activity with same balance
        onView(withId(R.id.balance_chip))
            .check(matches(withText(containsString("500"))))

        scenario.close()
    }

    @Test
    fun testNavigateThroughMultipleCars() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Navigate through 3 cars
        repeat(3) {
            onView(withId(R.id.next_button))
                .perform(click())

            // Verify car details are displayed
            onView(withId(R.id.car_name))
                .check(matches(isDisplayed()))
            onView(withId(R.id.car_image))
                .check(matches(isDisplayed()))
        }

        scenario.close()
    }

    @Test
    fun testThemeToggleFlow() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Toggle theme on
        onView(withId(R.id.theme_switch))
            .perform(click())

        // Verify theme switch is still displayed
        onView(withId(R.id.theme_switch))
            .check(matches(isDisplayed()))

        // Toggle theme off
        onView(withId(R.id.theme_switch))
            .perform(click())

        // Verify theme switch is still displayed
        onView(withId(R.id.theme_switch))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testFavoriteAndUnfavoriteFlow() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Add to favorites
        onView(withId(R.id.favorite_button))
            .perform(click())

        // Verify favorites RecyclerView is displayed
        onView(withId(R.id.favorites_recycler_view))
            .check(matches(isDisplayed()))

        // Remove from favorites
        onView(withId(R.id.favorite_button))
            .perform(click())

        // Verify favorites RecyclerView is still displayed
        onView(withId(R.id.favorites_recycler_view))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testDetailActivityComponentsAfterNavigation() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Click rent button
        onView(withId(R.id.rent_button))
            .perform(click())

        // Verify all detail activity components are displayed
        onView(withId(R.id.car_name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.car_image))
            .check(matches(isDisplayed()))
        onView(withId(R.id.days_slider))
            .check(matches(isDisplayed()))
        onView(withId(R.id.total_cost_text))
            .check(matches(isDisplayed()))
        onView(withId(R.id.save_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cancel_button))
            .check(matches(isDisplayed()))

        scenario.close()
    }
}
