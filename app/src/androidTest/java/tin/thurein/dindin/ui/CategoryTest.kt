package tin.thurein.dindin.ui

import android.content.Intent
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
import tin.thurein.dindin.activities.CategoryActivity
import tin.thurein.dindin.activities.MainActivity
import tin.thurein.dindin.selectTabAtPosition

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CategoryTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order =  1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun categoryTest() {
        activityRule.scenario.onActivity {
            val intent = Intent(it, CategoryActivity::class.java)
            it.startActivity(intent)
        }
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
            .perform(ViewActions.typeText("hello"))

        Thread.sleep(3000)

        Espresso.onView(ViewMatchers.withId(R.id.tlCategory))
            .perform(selectTabAtPosition(2))

        Thread.sleep(3000)

        Espresso.onView(ViewMatchers.withId(R.id.vpIngredient))
            .perform(ViewActions.swipeRight())

        Thread.sleep(3000)
    }
}