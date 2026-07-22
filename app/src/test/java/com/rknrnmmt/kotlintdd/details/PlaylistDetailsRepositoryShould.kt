package com.rknrnmmt.kotlintdd.details

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.models.Playlist
import com.rknrnmmt.kotlintdd.models.PlaylistRaw
import com.rknrnmmt.kotlintdd.playlist.PlaylistRepository
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaylistDetailsRepositoryShould: BaseUnitTest() {
    private val service : PlaylistDetailsService = mock()
    private val mapper : PlaylistDetailsMapper = mock()
    private val playlist = mock<Playlist>()
    private val playlistRaw = mock<PlaylistRaw>()
    private val exception = RuntimeException("Something went wrong")

    private val id = "1"
    @Test
    fun getPlaylistDetailsFromService(): Unit = runBlocking {
        val repository = mockSuccessfulCase()
        repository.getPlaylistDetailsById(id)
        verify(service, times(1)).fetchPlaylistDetailsById(id)
    }

    @Test
    fun emitMappedPlaylistsFromService() = runBlocking {
        val repository = mockSuccessfulCase()
        assertEquals(playlist, repository.getPlaylistDetailsById(id).first().getOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylistDetailsById(id).first()

        verify(mapper, times(1)).invoke(playlistRaw)
    }
    @Test
    fun propagateErrors() = runBlocking {
        whenever(service.fetchPlaylistDetailsById(id)).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )
        val repository = PlaylistDetailsRepository(service, mapper)

        assertEquals(exception, repository.getPlaylistDetailsById(id).first().exceptionOrNull())
    }

    private suspend fun mockSuccessfulCase(): PlaylistDetailsRepository {
        whenever(service.fetchPlaylistDetailsById(id)).thenReturn(
            flow {
                emit(Result.success(playlistRaw))
            }
        )

        whenever(mapper.invoke(playlistRaw)).thenReturn(playlist)

        return PlaylistDetailsRepository(service, mapper)
    }
}