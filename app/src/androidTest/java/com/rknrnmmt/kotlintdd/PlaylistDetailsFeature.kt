package com.rknrnmmt.kotlintdd

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import org.hamcrest.Matchers.allOf
import org.junit.Test

class PlaylistDetailsFeature : BaseUITest() {

    @Test
    fun displayPlaylistNameAndDetails() {
        openFirstPlaylistDetails()

        onView(withId(R.id.playlist_title))
            .check(matches(withText("Hard Rock Cafe")))
            .check(matches(isDisplayed()))

        onView(withId(R.id.playlist_description))
            .check(matches(withText("A collection of classic and modern rock songs.")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayLoaderWhileLoadingPlaylistDetails(){
        // First confirm the initial list has finished loading.
        onView(
            allOf(
                withId(R.id.playlist_image),
                isDescendantOfA(nthChildOf(withId(R.id.playlists_list), 0))
            )
        ).check(matches(isDisplayed()))

        stopWaitingForHttp()

        openFirstPlaylistDetails()

        assertDisplayed(R.id.loader)
    }

    @Test
    fun hideLoaderAfterPlaylistDetailsFetched(){
        openFirstPlaylistDetails()

        assertNotDisplayed(R.id.loader)
    }

    @Test
    fun hideLoaderOnErrorPlaylistDetailsFetch(){
        openFirstPlaylistDetails()

        assertNotDisplayed(R.id.loader)
    }

    @Test
    fun displaysErrorMessageWhenNetworkFails(){
        openFailingPlaylistDetails()

        assertDisplayed(R.string.something_went_wrong)
    }

}