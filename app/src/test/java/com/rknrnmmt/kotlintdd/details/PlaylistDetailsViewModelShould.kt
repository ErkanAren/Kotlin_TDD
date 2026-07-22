package com.rknrnmmt.kotlintdd.details

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rknrnmmt.kotlintdd.models.Playlist
import com.rknrnmmt.kotlintdd.utils.BaseUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest


@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistDetailsViewModelShould: BaseUnitTest() {

    private val repository: PlaylistDetailsRepository = mock()
    private val playlist = mock<Playlist>()
    private val expected = Result.success(playlist)
    private val exception = RuntimeException("Something went wrong")

    private lateinit var viewModel: PlaylistDetailsViewModel

    private val id = "1"

    @Test
    fun getPlaylistDetailsFromRepository() = runTest {
        viewModel = mockSuccessfulCase()

        viewModel.getPlaylistDetailsById(id).captureValues {
            advanceUntilIdle()
        }
        verify(repository, times(1)).getPlaylistDetailsById("1")
    }

    @Test
    fun emitErrorWhenReceiveError() = runTest {
        viewModel = mockErrorCase()

        viewModel.getPlaylistDetailsById(id).captureValues {
            advanceUntilIdle()
            assertEquals(exception, values.last()!!.exceptionOrNull())
        }
    }

    @Test
    fun emitPlaylistsFromRepository()= runTest {
        viewModel = mockSuccessfulCase()

        viewModel.getPlaylistDetailsById(id).captureValues {
            advanceUntilIdle()
            assertEquals(expected, values.last())
        }
    }

    @Test
    fun showSpinnerWhileLoading() = runTest {
        val viewmodel = mockSuccessfulCase()


        viewmodel.loader.captureValues {
            viewmodel.getPlaylistDetailsById(id).getValueForTest()

            advanceUntilIdle()

            assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runTest {
        val viewmodel = mockSuccessfulCase()

        viewmodel.loader.captureValues {
            viewmodel.getPlaylistDetailsById(id).captureValues {
                advanceUntilIdle()
            }

            assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runTest {
        val viewmodel = mockErrorCase()

        viewmodel.loader.captureValues {
            viewmodel.getPlaylistDetailsById(id).captureValues {
                advanceUntilIdle()
            }

            assertEquals(false, values.last())
        }
    }

    private suspend fun mockErrorCase(): PlaylistDetailsViewModel {
        whenever(repository.getPlaylistDetailsById(id)).thenReturn(
            flow { emit(Result.failure<Playlist>(exception)) }
        )
        return PlaylistDetailsViewModel(repository)
    }

    private suspend fun mockSuccessfulCase(): PlaylistDetailsViewModel {
        whenever(repository.getPlaylistDetailsById(id)).thenReturn(
            flow { emit(expected) }
        )
        return PlaylistDetailsViewModel(repository)
    }
}