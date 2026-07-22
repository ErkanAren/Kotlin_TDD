package com.rknrnmmt.kotlintdd

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.rknrnmmt.kotlintdd.network.PlaylistHttpClient
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description as JUnitDescription
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {

    private val okHttpIdlingResource =
        OkHttpIdlingResource(PlaylistHttpClient.instance)

    private val idlingResourceRule = object : TestWatcher() {
        override fun starting(description: JUnitDescription) {
            IdlingRegistry.getInstance().register(okHttpIdlingResource)
        }

        override fun finished(description: JUnitDescription) {
            IdlingRegistry.getInstance().unregister(okHttpIdlingResource)
            okHttpIdlingResource.close()
        }
    }

    private val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rules: TestRule = RuleChain
        .outerRule(idlingResourceRule)
        .around(activityRule)

    protected fun openFirstPlaylistDetails() {
        onView(
            allOf(
                withId(R.id.playlist_image),
                isDescendantOfA(nthChildOf(withId(R.id.playlists_list), 0))
            )
        ).perform(click())
    }

    protected fun openFailingPlaylistDetails() {
        clickListItem(R.id.playlists_list, 9)
    }



    protected fun stopWaitingForHttp() {
        IdlingRegistry.getInstance().unregister(okHttpIdlingResource)
    }

    protected fun nthChildOf(
        parentMatcher: Matcher<View>,
        childPosition: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("position $childPosition of parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup

                return parentMatcher.matches(parent) &&
                    parent.childCount > childPosition &&
                    parent.getChildAt(childPosition) == view
            }
        }
    }
}
