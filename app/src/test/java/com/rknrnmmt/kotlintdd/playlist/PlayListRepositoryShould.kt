package com.rknrnmmt.kotlintdd.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class PlayListRepositoryShould : BaseUnitTest() {
    private val service : PlaylistService = mock()
    private val playlists = mock<List<Playlist>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistsFromService(): Unit = runBlocking {
        val repository = PlaylistRepository(service)
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPLaylistsFromService() = runBlocking {
        val repository = mockSuccessfulCase()
        assertEquals(playlists, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runBlocking {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )
        val repository = PlaylistRepository(service)

        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlists))
            }
        )

        val repository = PlaylistRepository(service)
        return repository
    }

}