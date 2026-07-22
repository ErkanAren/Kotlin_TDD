package com.rknrnmmt.kotlintdd.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.models.Playlist
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class PlayListRepositoryShould : BaseUnitTest() {
    private val service : PlaylistService = mock()
    val mapper : PlaylistMapper = mock()
    private val playlists = mock<List<Playlist>>()
    private val playlistsRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistsFromService(): Unit = runBlocking {
        val repository = mockSuccessfulCase()
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitMappedPlaylistsFromService() = runBlocking {
        val repository = mockSuccessfulCase()
        assertEquals(playlists, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylists().first()

        verify(mapper, times(1)).invoke(playlistsRaw)
    }
    @Test
    fun propagateErrors() = runBlocking {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )
        val repository = PlaylistRepository(service, mapper)

        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistsRaw))
            }
        )

        whenever(mapper.invoke(playlistsRaw)).thenReturn(playlists)

        return PlaylistRepository(service, mapper)
    }

}