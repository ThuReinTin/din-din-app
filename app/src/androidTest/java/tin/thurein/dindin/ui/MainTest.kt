package tin.thurein.dindin.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tin.thurein.dindin.R
import tin.thurein.dindin.activities.MainActivity

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order =  1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        // Context of the app under test.
        Espresso.onView(ViewMatchers.withId(R.id.ivNext))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.ivIngredient))
            .perform(ViewActions.click())
    }
}