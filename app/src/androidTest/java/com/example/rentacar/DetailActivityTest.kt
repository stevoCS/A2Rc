package com.example.rentacar

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentacar.model.Car
import com.example.rentacar.ui.detail.DetailActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for DetailActivity
 * Tests rental detail screen including slider, validation, and buttons
 */
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    private fun createTestCar(): Car {
        return Car(
            id = "1",
            name = "BMW",
            model = "i7",
            year = 2024,
            rating = 4.9f,
            km = 5000,
            dailyCost = 150,
            imageRes = R.drawable.bmw_i7
        )
    }

    private fun launchDetailActivity(): ActivityScenario<DetailActivity> {
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra("car", createTestCar())
            putExtra("balance", 500)
        }
        return ActivityScenario.launch(intent)
    }

    @Test
    fun testCarDetailsDisplayed() {
        val scenario = launchDetailActivity()

        // Verify car name and image are displayed
        onView(withId(R.id.car_name))
            .check(matches(isDisplayed()))
            .check(matches(withText("BMW i7")))
        onView(withId(R.id.car_image))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testDaysSliderDisplayed() {
        val scenario = launchDetailActivity()

        // Verify days slider and selected days text are displayed
        onView(withId(R.id.days_slider))
            .check(matches(isDisplayed()))
        onView(withId(R.id.selected_days_text))
            .check(matches(withText("Selected Days: 1")))

        scenario.close()
    }

    @Test
    fun testInitialCostCalculation() {
        val scenario = launchDetailActivity()

        // Verify initial cost is 150 (1 day * 150 daily cost)
        onView(withId(R.id.total_cost_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("Total Cost: 150 credits")))

        scenario.close()
    }

    @Test
    fun testSaveButtonEnabledInitially() {
        val scenario = launchDetailActivity()

        // Verify save button is enabled initially (cost 150 is valid)
        onView(withId(R.id.save_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("Rent")))
            .check(matches(isEnabled()))

        scenario.close()
    }

    @Test
    fun testCancelButtonFunctionality() {
        val scenario = launchDetailActivity()

        // Verify cancel button is displayed and clickable
        onView(withId(R.id.cancel_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("Cancel")))
            .perform(click())

        // Wait a moment for activity to finish
        Thread.sleep(500)

        scenario.close()
    }
}
