package com.rknrnmmt.kotlintdd.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rknrnmmt.kotlintdd.playlist.PlaylistRepository
import javax.inject.Inject

class PlaylistDetailsViewModelFactory @Inject constructor(
    private val repository: PlaylistDetailsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistDetailsViewModel(repository) as T
    }
}
