package com.rknrnmmt.kotlintdd.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaylistServiceShould : BaseUnitTest(){

    private lateinit var service:PlaylistService
    private var api : PlaylistAPI = mock()
    private var playlists : List<Playlist> = mock()

    @Test
    fun fetchPlaylistsFromAPI(): Unit = runBlocking {
        service = PlaylistService(api)
        service.fetchPlaylists().first()
        verify(api,times(1)).fetchPlaylists()
    }

    @Test
    fun covertsValuesAndEmitsThem() = runBlocking {
        mockSuccessfulCase()

        assertEquals(Result.success(playlists),service.fetchPlaylists().first())
    }

    @Test
    fun emitFailureResultWhenNetworkFails() = runBlocking {
        mockFailureCase()

        assertEquals("Something went wrong",
            service.fetchPlaylists().first().exceptionOrNull()?.message)
    }

    private suspend fun mockSuccessfulCase() {
        whenever(api.fetchPlaylists()).thenReturn(playlists)
        service = PlaylistService(api)
    }

    private suspend fun mockFailureCase() {
        whenever(api.fetchPlaylists()).thenThrow(RuntimeException("Gift from backend developers"))
        service = PlaylistService(api)
    }
}