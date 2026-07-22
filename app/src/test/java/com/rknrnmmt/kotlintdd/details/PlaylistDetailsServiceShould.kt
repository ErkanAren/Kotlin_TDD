package com.rknrnmmt.kotlintdd.details

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.network.PlaylistAPI
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class PlaylistDetailsServiceShould: BaseUnitTest() {
    private val api : PlaylistAPI = mock()
    private lateinit var service: PlaylistDetailsService
    private val id = "1"
    private val playlistRaw : PlaylistRaw = mock()

    @Test
    fun fetchPlaylistsFromAPI(): Unit = runBlocking {
        service = PlaylistDetailsService(api)
        service.fetchPlaylistDetailsById(id).first()
        verify(api,times(1)).fetchPlaylistDetailsById(id)
    }

    @Test
    fun covertsValuesAndEmitsThem() = runBlocking {
        mockSuccessfulCase()

        assertEquals(Result.success(playlistRaw),service.fetchPlaylistDetailsById(id).first())
    }

    @Test
    fun emitFailureResultWhenNetworkFails() = runBlocking {
        mockFailureCase()

        assertEquals("Something went wrong",
            service.fetchPlaylistDetailsById(id).first().exceptionOrNull()?.message)
    }

    private suspend fun mockSuccessfulCase(){
        whenever(api.fetchPlaylistDetailsById(id)).thenReturn(playlistRaw)
        service = PlaylistDetailsService(api)
    }

    private suspend fun mockFailureCase(){
        whenever(api.fetchPlaylistDetailsById(id)).thenThrow(RuntimeException("Gift from backend developers"))
        service = PlaylistDetailsService(api)
    }
}