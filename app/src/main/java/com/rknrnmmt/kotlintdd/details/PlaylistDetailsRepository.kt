package com.rknrnmmt.kotlintdd.details

import com.rknrnmmt.kotlintdd.R
import com.rknrnmmt.kotlintdd.models.Playlist
import com.rknrnmmt.kotlintdd.playlist.PlaylistMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistDetailsRepository @Inject constructor(
    private val service: PlaylistDetailsService,
    private val mapper: PlaylistDetailsMapper
){

    suspend fun getPlaylistDetailsById(id:String): Flow<Result<Playlist>> =
        service.fetchPlaylistDetailsById(id).map { it ->
            if (it.isSuccess) {
                Result.success(mapper.invoke(it.getOrNull()!!))
            }
            else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

}
