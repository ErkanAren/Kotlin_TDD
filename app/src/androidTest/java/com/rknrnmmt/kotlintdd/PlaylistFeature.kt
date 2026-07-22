package com.rknrnmmt.kotlintdd

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.internal.matcher.DrawableMatcher.Companion.withDrawable
import org.hamcrest.core.AllOf.allOf
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlaylistFeature : BaseUITest() {

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
        openFirstPlaylistDetails()

        assertDisplayed(R.id.playlist_title)
        assertDisplayed(R.id.playlist_description)

    }

}