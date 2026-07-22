package com.rknrnmmt.kotlintdd

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.internal.matcher.DrawableMatcher.Companion.withDrawable
import com.rknrnmmt.kotlintdd.playlist.PlaylistHttpClient
import com.rknrnmmt.kotlintdd.playlist.models.Playlist
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf

import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.junit.runner.Description as JUnitDescription

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PlaylistFeature {
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

    @Test
    fun displayScreenTitle() {
        // using barista
        assertDisplayed("Playlists")
    }

    @Test
    fun displayListOfPlaylists(){
        assertRecyclerViewItemCount(R.id.playlists_list,10)

        onView(allOf(withId(R.id.playlist_name), isDescendantOfA(nthChildOf(withId(R.id.playlists_list),0))))
            .check(matches(withText("Hard Rock Cafe")))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.playlist_category), isDescendantOfA(nthChildOf(withId(R.id.playlists_list),0))))
            .check(matches(withText("rock")))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.playlist_image), isDescendantOfA(nthChildOf(withId(R.id.playlists_list),1))))
            .check(matches(withDrawable(R.mipmap.playlist)))
            .check(matches(isDisplayed()))
    }


    @Test
    fun hidesLoader(){
        assertNotDisplayed(R.id.loader)
    }

    @Test
    fun displaysRockImageForRockListItems(){
        onView(allOf(withId(R.id.playlist_image), isDescendantOfA(nthChildOf(withId(R.id.playlists_list),0))))
            .check(matches(withDrawable(R.mipmap.rock)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToDetailsScreen(){
        onView(allOf(withId(R.id.playlist_image), isDescendantOfA(nthChildOf(withId(R.id.playlists_list),0))))
            .perform(click())

        assertDisplayed(R.id.playlist_details_root)
    }

    // from parentview access the nth child
    fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("position $childPosition of parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup

                return (parentMatcher.matches(parent)
                        && parent.childCount > childPosition
                        && parent.getChildAt(childPosition) == view)
            }
        }
    }

}

private class OkHttpIdlingResource(
    private val client: OkHttpClient
) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName() = "OkHttp"

    override fun isIdleNow(): Boolean {
        val idle = client.dispatcher.runningCallsCount() == 0
        if (idle) callback?.onTransitionToIdle()
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
        client.dispatcher.idleCallback = Runnable {
            callback.onTransitionToIdle()
        }
    }

    fun close() {
        callback = null
        client.dispatcher.idleCallback = null
    }
}