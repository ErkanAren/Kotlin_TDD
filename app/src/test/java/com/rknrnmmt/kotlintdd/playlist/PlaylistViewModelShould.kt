package com.rknrnmmt.kotlintdd.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import petros.efthymiou.groovy.utils.captureValues

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistViewModelShould: BaseUnitTest() {

    private val repository: PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong")

    private lateinit var viewModel: PlaylistViewModel


    @Test
    fun getPlaylistsFromRepository() = runTest {
        viewModel = mockSuccessfulCase()

        viewModel.playlists.captureValues {
            advanceUntilIdle()
        }
        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitErrorWhenReceiveError() = runTest {
        viewModel = mockErrorCase()

        viewModel.playlists.captureValues {
            advanceUntilIdle()
            assertEquals(exception, values.last()!!.exceptionOrNull())
        }
    }

    @Test
    fun emitPlaylistsFromRepository()= runTest {
        viewModel = mockSuccessfulCase()

        viewModel.playlists.captureValues {
            advanceUntilIdle()
            assertEquals(expected, values.last())
        }
    }

    @Test
    fun showSpinnerWhileLoading() = runTest {
        val viewmodel = mockSuccessfulCase()


        viewmodel.loader.captureValues {
            viewmodel.playlists.getValueForTest()

            advanceUntilIdle()

            assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runTest {
        val viewmodel = mockSuccessfulCase()

        viewmodel.loader.captureValues {
            viewmodel.playlists.captureValues {
                advanceUntilIdle()
            }

            assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runTest {
        val viewmodel = mockErrorCase()

        viewmodel.loader.captureValues {
            viewmodel.playlists.captureValues {
                advanceUntilIdle()
            }

            assertEquals(false, values.last())
        }
    }

    private suspend fun mockErrorCase(): PlaylistViewModel {
        whenever(repository.getPlaylists()).thenReturn(
            flow { emit(Result.failure<List<Playlist>>(exception)) }
        )
        return PlaylistViewModel(repository)
    }

    private suspend fun mockSuccessfulCase(): PlaylistViewModel {
        whenever(repository.getPlaylists()).thenReturn(
            flow { emit(expected) }
        )
        return PlaylistViewModel(repository)
    }
}