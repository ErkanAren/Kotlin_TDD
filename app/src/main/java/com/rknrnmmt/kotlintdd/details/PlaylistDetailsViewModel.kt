package com.rknrnmmt.kotlintdd.details

import androidx.lifecycle.*
import com.rknrnmmt.kotlintdd.models.Playlist
import kotlinx.coroutines.flow.onEach

class PlaylistDetailsViewModel (
    private val repository: PlaylistDetailsRepository
        ):ViewModel() {

    val loader = MutableLiveData<Boolean>()

    fun getPlaylistDetailsById(id: String) = liveData<Result<Playlist>> {
        loader.postValue(true)

        emitSource(
            repository.getPlaylistDetailsById(id)
                .onEach { loader.postValue(false) }
                .asLiveData()
        )
    }

}
