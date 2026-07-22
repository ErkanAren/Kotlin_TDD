package com.rknrnmmt.kotlintdd

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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

}