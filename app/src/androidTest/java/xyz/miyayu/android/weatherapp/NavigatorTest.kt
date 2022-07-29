package xyz.miyayu.android.weatherapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import xyz.miyayu.android.weatherapp.views.fragments.TopFragment

@RunWith(AndroidJUnit4::class)
class NavigatorTest {
    @Test
    fun testNavigation() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val topScenario = launchFragmentInContainer<TopFragment>(null, R.style.Theme_WeatherApp)

        topScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(ViewMatchers.withId(R.id.setting_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.settingFragment)
    }
}