package com.rknrnmmt.kotlintdd.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import petros.efthymiou.groovy.utils.getValueForTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlaylistViewModelShould: BaseUnitTest() {

    private val repository: PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong")

    private lateinit var viewModel: PlaylistViewModel


    @Test
    fun getPlaylistsFromRepository() = runTest {
        initialize()

        viewModel.playlists.getValueForTest()

        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitErrorWhenReceiveError() {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(Result.failure(exception))
                }
            )
        }
        viewModel = PlaylistViewModel(repository)
        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull()!!)
    }

    @Test
    fun emitPlaylistsFromRepository()= runTest {
        initialize()
        assertEquals(expected, viewModel.playlists.getValueForTest())
    }

    fun initialize(){
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }

        viewModel = PlaylistViewModel(repository)
    }
}